package com.vivek.githubtrending.data;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.vivek.githubtrending.data.remote.model.ApiResponse;
import com.vivek.githubtrending.util.AppExecutors;

import timber.log.Timber;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<ResultType>> results = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        init();
    }


    private void init() {

        // update LiveData for loading status
        results.setValue((Resource<ResultType>) Resource.loading(null));

        // observe LiveData source from local db
        final LiveData<ResultType> dbSource = loadFromDb();

        results.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType ResultType) {

                results.removeSource(dbSource);

                if (shouldFetch(ResultType)) {
                    // get data from the network
                    fetchFromNetwork(dbSource);
                } else {
                    results.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType ResultType) {
                            setValue(Resource.success(ResultType));
                        }
                    });
                }
            }
        });
    }

    /**
     * 1) observe local db
     * 2) if <condition/> query the network
     * 3) stop observing the local db
     * 4) insert new data into local db
     * 5) begin observing local db again to see the refreshed data from network
     *
     * @param dbSource
     */
    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {

        Timber.d("fetchFromNetwork: called.");

        // update LiveData for loading status
        results.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType ResultType) {
                setValue(Resource.loading(ResultType));
            }
        });

        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        results.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestType> requestObjectApiResponse) {
                results.removeSource(dbSource);
                results.removeSource(apiResponse);

                /*
                    3 cases:
                       1) ApiSuccessResponse
                       2) ApiErrorResponse
                       3) ApiEmptyResponse
                 */

                if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {
                    Timber.d("onChanged: ApiSuccessResponse.");

                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            // save the response to the local db
                            saveCallResult((RequestType) processResponse((ApiResponse.ApiSuccessResponse) requestObjectApiResponse));

                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    results.addSource(loadFromDb(), new Observer<ResultType>() {
                                        @Override
                                        public void onChanged(@Nullable ResultType ResultType) {
                                            setValue(Resource.success(ResultType));
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {
                    Timber.d("onChanged: ApiEmptyResponse");
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            results.addSource(loadFromDb(), new Observer<ResultType>() {
                                @Override
                                public void onChanged(@Nullable ResultType ResultType) {
                                    setValue(Resource.success(ResultType));
                                }
                            });
                        }
                    });
                } else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {
                    Timber.d("onChanged: ApiErrorResponse.");
                    results.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType ResultType) {

                            setValue(
                                    Resource.error(
                                            ((ApiResponse.ApiErrorResponse) requestObjectApiResponse).getErrorMessage(),
                                            ResultType
                                    )
                            );
                        }
                    });
                }
            }
        });
    }

    private ResultType processResponse(ApiResponse.ApiSuccessResponse response) {
        return (ResultType) response.getBody();
    }

    private void setValue(Resource<ResultType> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return results;
    }

}