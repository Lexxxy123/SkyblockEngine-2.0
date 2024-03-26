package net.hypixel.skyblock.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import net.hypixel.skyblock.item.MaterialQuantifiable;
import net.hypixel.skyblock.item.ShapelessRecipe;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class RecipeDatabase {

    public static void loadRecipes() {

        FindIterable<Document> recipeDocuments = DatabaseManager.getCollection("recipes").join().find();

        try (MongoCursor<Document> cursor = recipeDocuments.iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                ShapelessRecipe.fromDocument(doc);
            }
        }

    }



    public static void save(ShapelessRecipe recipe) {
        List<Document> ingredientList = new ArrayList<>();
        for (MaterialQuantifiable mq : recipe.getIngredientList()) {
            Document ingredientDoc = new Document("material", mq.getMaterial().name())
                    .append("amount", mq.getAmount());
            ingredientList.add(ingredientDoc);
        }

        Document doc = new Document("result", recipe.getResult().getType().name())
                .append("amount" , recipe.getResult().getStack().getAmount())
                .append("ingredientList", ingredientList);

        DatabaseManager.getCollection("recipes").join().insertOne(doc);
    }
}