package oglasi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name="recenzije")
public class Recenzije {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;



    @Column(nullable = false)
    @NotBlank(message = "Unesite opis  oglasa.")
    String opis;



    @ManyToOne
    @JoinColumn(name = "recenzija_id", nullable = true)
    Recenzije parent;

    @OneToMany(mappedBy = "parent")
    List<Recenzije> recenzija;

    @ManyToOne
    @JoinColumn(name = "oglas_id")
    private Oglasi oglasi;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;



    public Recenzije(Long id, String opis) {
        this.id = id;
        this.opis = opis;

    }

    public Recenzije() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }



    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }






    public Recenzije getParent() {
        return parent;
    }

    public void setParent(Recenzije parent) {
        this.parent = parent;
    }

    public Oglasi getOglasi() {
        return oglasi;
    }

    public void setOglasi(Oglasi oglasi) {
        this.oglasi = oglasi;
    }

    public Recenzije(User createdBy) {
        this.createdBy = createdBy;
    }



    public User getUser() {
        return createdBy;
    }

    public void setUser(User createdBy) {
        this.createdBy = createdBy;
    }
}
