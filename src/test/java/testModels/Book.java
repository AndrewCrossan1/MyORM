package testModels;

import me.andrewc.Annotations.Column;
import me.andrewc.Annotations.Types.*;
import me.andrewc.Annotations.Entity;
import me.andrewc.Annotations.Types.Number;
import me.andrewc.Models.Model;

import java.math.BigDecimal;

@Entity(tableName = "books")
public class Book extends Model {

    @Column
    @Varchar(length = 255)
    private String title;

    @Column
    @Decimal(value = "10,2")
    private BigDecimal price;

    @Column
    @Text(length = 255)
    private String description;

    @Column
    @Number(max = 1000)
    private int pages;

    @Column
    @Date
    private String createdAt;

    @Column
    @DateTime
    private String updatedAt;

    public Book(String title, BigDecimal price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public Book(String title, BigDecimal price, String description, int pages) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.pages = pages;
    }

    public Book(String title, BigDecimal price, String description, int pages, String createdAt, String updatedAt) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.pages = pages;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Book() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
