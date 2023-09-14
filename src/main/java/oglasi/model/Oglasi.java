package oglasi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name="oglasi")
public class Oglasi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @NotBlank(message = "Unesite naziv oglasa.")
    String name;

    @Column(nullable = false)
    @NotBlank(message = "Unesite opis  oglasa.")
    String opis;

    @Column(nullable = false)
    @NotBlank(message = "Unesite cijenu  oglasa.")
    String cijena;

    @ManyToOne
    @JoinColumn(name = "oglas_id", nullable = true)
    Oglasi parent;

    @OneToMany(mappedBy = "parent")
    List<Oglasi> oglas;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "oglasi")
    List<Recenzije> recenzija;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;



    public Oglasi(Long id, String name, String opis, String cijena) {
        this.id = id;
        this.name = name;
        this.opis = opis;
        this.cijena = cijena;
    }

    public Oglasi() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getCijena() {
        return cijena;
    }

    public void setCijena(String cijena) {
        this.cijena = cijena;
    }


    public Oglasi getParent() {
        return parent;
    }

    public void setParent(Oglasi parent) {
        this.parent = parent;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Oglasi(User createdBy) {
        this.createdBy = createdBy;
    }



    public User getUser() {
        return createdBy;
    }

    public void setUser(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Recenzije> getRecenzija() {
        return recenzija;
    }

    public void setRecenzija(List<Recenzije> recenzija) {
        this.recenzija = recenzija;
    }
}
