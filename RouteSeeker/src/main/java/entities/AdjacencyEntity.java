package entities;

import javax.persistence.*;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdGraph() {
        return idGraph;
    }

    public void setIdGraph(Integer idGraph) {
        this.idGraph = idGraph;
    }

    public Integer getIdNode1() {
        return idNode1;
    }

    public void setIdNode1(Integer idNode1) {
        this.idNode1 = idNode1;
    }

    public Integer getIdNode2() {
        return idNode2;
    }

    public void setIdNode2(Integer idNode2) {
        this.idNode2 = idNode2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjacencyEntity that = (AdjacencyEntity) o;

        if (id != that.id) return false;
        if (idGraph != null ? !idGraph.equals(that.idGraph) : that.idGraph != null) return false;
        if (idNode1 != null ? !idNode1.equals(that.idNode1) : that.idNode1 != null) return false;
        if (idNode2 != null ? !idNode2.equals(that.idNode2) : that.idNode2 != null) return false;

        return true;
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
