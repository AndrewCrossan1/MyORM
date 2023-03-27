package me.andrewc.Models;

import me.andrewc.Annotations.Column;
import me.andrewc.Annotations.Constraints.NotNull;
import me.andrewc.Annotations.Constraints.PrimaryKey;
import me.andrewc.Annotations.Id;

import java.util.UUID;

/**
 * Defines the base model for all entities.
 * @field ID The id of the entity. (UUID)
 * @method getId() Returns the id of the entity.
 * @method setId(int id) Sets the id of the entity.
 */
public abstract class Model {

    @Column
    @Id
    @NotNull
    @PrimaryKey
    private final UUID ID;

    public UUID getID() {
        return ID;
    }

    public Model() {
        this.ID = UUID.randomUUID();
   }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " { ID: " + this.ID + " }";
    }
}
