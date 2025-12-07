package source;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ratings")
public class Rating {
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
    private Horse horse;

    public Rating() {}

    public Rating(int value, Horse horse, String description) {
        this.value = value;
        this.horse = horse;
        this.description = description;
        this.date = new Date();
    }
}