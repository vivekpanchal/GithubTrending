package com.vivek.githubtrending.data;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private Observable<Resource<ResultType>> result;

    @MainThread
    protected NetworkBoundResource() {
        init();
    }

    private void init() {
        Observable<Resource<ResultType>> source;
        if (shouldFetch()) {
            source = createCall()
                    .subscribeOn(Schedulers.io())
                    .doOnNext(apiResponse -> saveCallResult(processResponse(apiResponse)))
                    .flatMap(apiResponse -> loadFromDb().toObservable().map(Resource::success))
                    .doOnError(this::onFetchFailed) // Always log errors don't leave empty
                    .onErrorResumeNext(t -> {
                        return loadFromDb()
                                .toObservable()
                                .map(data -> Resource.error(t.getMessage(), data));

                    })
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            source = loadFromDb()
                    .toObservable()
                    .map(Resource::success);
        }

        result = Observable.concat(
                loadFromDb()
                        .toObservable()
                        .map(Resource::loading)
                        .take(1),
                source
        );
    }

    public Observable<Resource<ResultType>> getAsObservable() {return result;}

    // Always log errors don't leave empty
    protected void onFetchFailed(Throwable cause) {
        Timber.e(cause);
    }

    @WorkerThread
    protected RequestType processResponse(Resource<RequestType> response) {return response.data;}

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @MainThread
    protected abstract boolean shouldFetch();

    @NonNull
    @MainThread
    protected abstract Flowable<ResultType> loadFromDb();

    @NonNull
    @MainThread
    protected abstract Observable<Resource<RequestType>> createCall();
}