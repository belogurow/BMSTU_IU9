
package ru.belogurowdev.lab7.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Geometry{");
        sb.append("location=").append(location);
        sb.append(", viewport=").append(viewport);
        sb.append('}');
        return sb.toString();
    }

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}
