package com.vivek.githubtrending.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class GithubEntity implements Parcelable {

    public GithubEntity(@NonNull Long id, String author, String name, String avatar,
                        String url, String description, Integer stars, Integer forks, Integer currentPeriodStars, String language, String languageColor) {
        this.id = id;
        this.author = author;
        this.name = name;
        this.avatar = avatar;
        this.url = url;
        this.description = description;
        this.stars = stars;
        this.forks = forks;
        this.currentPeriodStars = currentPeriodStars;
        this.language = language;
        this.languageColor = languageColor;
    }

    @NonNull
    @PrimaryKey
    private Long id;

    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("stars")
    @Expose
    private Integer stars;
    @SerializedName("forks")
    @Expose
    private Integer forks;
    @SerializedName("currentPeriodStars")
    @Expose
    private Integer currentPeriodStars;

    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("languageColor")
    @Expose
    private String languageColor;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    protected GithubEntity(Parcel in) {
        author = in.readString();
        name = in.readString();
        avatar = in.readString();
        url = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            stars = null;
        } else {
            stars = in.readInt();
        }
        if (in.readByte() == 0) {
            forks = null;
        } else {
            forks = in.readInt();
        }
        if (in.readByte() == 0) {
            currentPeriodStars = null;
        } else {
            currentPeriodStars = in.readInt();
        }
        language = in.readString();
        languageColor = in.readString();
    }

    public static final Creator<GithubEntity> CREATOR = new Creator<GithubEntity>() {
        @Override
        public GithubEntity createFromParcel(Parcel in) {
            return new GithubEntity(in);
        }

        @Override
        public GithubEntity[] newArray(int size) {
            return new GithubEntity[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getCurrentPeriodStars() {
        return currentPeriodStars;
    }

    public void setCurrentPeriodStars(Integer currentPeriodStars) {
        this.currentPeriodStars = currentPeriodStars;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageColor() {
        return languageColor;
    }

    public void setLanguageColor(String languageColor) {
        this.languageColor = languageColor;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(url);
        dest.writeString(description);
        if (stars == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(stars);
        }
        if (forks == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(forks);
        }
        if (currentPeriodStars == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(currentPeriodStars);
        }
        dest.writeString(language);
        dest.writeString(languageColor);
    }
}
