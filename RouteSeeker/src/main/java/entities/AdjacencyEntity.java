package entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * Entity class for the <code>adjacency</code> table in <code>PostgreSQL</code>
 */
@Entity
@Table(name = "adjacency", schema = "public", catalog = "route_seeker")
public class AdjacencyEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "id_graph")
    private Integer idGraph;
    @Basic
    @Column(name = "id_node1")
    private Integer idNode1;
    @Basic
    @Column(name = "id_node2")
    private Integer idNode2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjacencyEntity that = (AdjacencyEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(idGraph, that.idGraph)) return false;
        if (!Objects.equals(idNode1, that.idNode1)) return false;
        return Objects.equals(idNode2, that.idNode2);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (idGraph != null ? idGraph.hashCode() : 0);
        result = 31 * result + (idNode1 != null ? idNode1.hashCode() : 0);
        result = 31 * result + (idNode2 != null ? idNode2.hashCode() : 0);
        return result;
    }
}
