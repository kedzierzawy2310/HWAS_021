package com.example.hwas_021;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.Random;

import static com.example.hwas_021.MainActivity.images;

public class Event implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String country;
    private int image;
    private String imageString;

    public Event(int id, String country, String title, String description, int image, String imageString) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.country = country;
        this.image = image;
        if(image<=5 && !imageString.contains(".jpg"))
        {
            switch (image){
                case 0:
                    imageString="basketball_ante";
                    break;
                case 1:
                    imageString="basketball_james";
                    break;
                case 2:
                    imageString="basketball_kyrie";
                    break;
                case 3:
                    imageString="basketball_rose";
                    break;
                case 4:
                    imageString="basketball_walker";
                    break;
                case 5:
                    imageString="basketball_wall";
                    break;
                default:
                    imageString="lana";
                    break;
            }
        }
        this.imageString = imageString;
    }

    public void setImage() {
        Random r = new Random();
        int i = r.nextInt(images.length);
        switch (i){
            case 0:
                imageString="basketball_ante";
                break;
            case 1:
                imageString="basketball_james";
                break;
            case 2:
                imageString="basketball_kyrie";
                break;
            case 3:
                imageString="basketball_rose";
                break;
            case 4:
                imageString="basketball_walker";
                break;
            case 5:
                imageString="basketball_wall";
                break;
            default:
                imageString="lana";
                break;
        }
        setImageString(imageString);
        this.image = i;
    }

    protected Event(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        country = in.readString();
        image = in.readInt();
        imageString = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getCountry() {
        return country;
    }

    public String getImageString() {
        return imageString;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(country);
        dest.writeInt(image);
        dest.writeString(imageString);
    }
}
