/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import java.util.ArrayList;
import net.hypixel.skyblock.database.DatabaseManager;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.ShapelessRecipe;
import org.bson.Document;

public class RecipeDatabase {
    public static void loadRecipes() {
        FindIterable<Document> recipeDocuments = DatabaseManager.getCollection("recipes").join().find();
        try (MongoCursor cursor = recipeDocuments.iterator();){
            while (cursor.hasNext()) {
                Document doc = (Document)cursor.next();
                ShapelessRecipe.fromDocument(doc);
            }
        }
    }

    public static void save(ShapelessRecipe recipe) {
        ArrayList<Document> ingredientList = new ArrayList<Document>();
        for (MaterialQuantifiable mq : recipe.getIngredientList()) {
            Document ingredientDoc = new Document("material", mq.getMaterial().name()).append("amount", mq.getAmount());
            ingredientList.add(ingredientDoc);
        }
        Document doc = new Document("result", recipe.getResult().getType().name()).append("amount", recipe.getResult().getStack().getAmount()).append("ingredientList", ingredientList);
        DatabaseManager.getCollection("recipes").join().insertOne(doc);
    }
}

