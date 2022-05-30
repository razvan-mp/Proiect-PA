package entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * Entity class for the <code>nodes</code> table in <code>PostgreSQL</code>
 */
@Entity
@Table(name = "nodes", schema = "public", catalog = "route_seeker")
public class NodesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "id_graph")
    private Integer idGraph;
    @Basic
    @Column(name = "latitude")
    private Double latitude;
    @Basic
    @Column(name = "longitude")
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodesEntity that = (NodesEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(idGraph, that.idGraph)) return false;
        if (!Objects.equals(latitude, that.latitude)) return false;
        return Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (idGraph != null ? idGraph.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }
}
