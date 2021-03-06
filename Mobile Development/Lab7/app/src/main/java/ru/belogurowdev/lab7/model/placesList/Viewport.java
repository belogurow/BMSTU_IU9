
package ru.belogurowdev.lab7.model.placesList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("northeast")
    @Expose
    private Northeast northeast;
    @SerializedName("southwest")
    @Expose
    private Southwest southwest;

    public Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Viewport{");
        sb.append("northeast=").append(northeast);
        sb.append(", southwest=").append(southwest);
        sb.append('}');
        return sb.toString();
    }
}
