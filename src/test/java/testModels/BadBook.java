package testModels;

import me.andrewc.Annotations.Types.Decimal;
import me.andrewc.Annotations.Entity;
import me.andrewc.Annotations.Types.Text;
import me.andrewc.Annotations.Types.Varchar;
import me.andrewc.Models.Model;

import java.util.UUID;

@Entity(tableName = "books")
public class BadBook extends Model {

    @Varchar(length = 255)
    private int title;

    @Decimal(value = "10,2")
    private UUID price;

    @Text(length = 255)
    private int description;

    public BadBook(int title, UUID price, int description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }
    public BadBook() {
        super();
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public UUID getPrice() {
        return price;
    }

    public void setPrice(UUID price) {
        this.price = price;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }
}
