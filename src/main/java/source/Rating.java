package source;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ratings")
public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating_value", nullable = false)
    private int value;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String description;

    @ManyToOne
    @JoinColumn(name = "horse_id", nullable = false)
    @JsonIgnore
    private Horse horse;

    public Rating() {}

    public Rating(int value, Horse horse, String description) {
        this.value = value;
        this.horse = horse;
        this.description = description;
        this.date = new Date();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getValue() { return value; }
}