package P3;

import java.io.*;
import java.util.ArrayList;

public class DataController {
    public static void saveIngridients(ArrayList<Ingridient> ingridients) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Ingridients.txt",false));
        StringBuilder sb = new StringBuilder();
        for(Ingridient x : ingridients) {
            sb.append(x.getName() +"/"+x.getTaste().toString()+"\n");
        }
        //System.out.println(sb.toString());
        bufferedWriter.append(sb.toString());
        bufferedWriter.close();
    }

    public static void loadIngridients() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Ingridients.txt"));
            String data;
            while((data = bufferedReader.readLine()) != null) {
                String[] vars = data.split("[/]");
                Ingridient tmp = new Ingridient(vars[0], Ingridient.TASTE.valueOf(vars[1]));
                Recipe.addToListOfAllIngridients(tmp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveRecipes(ArrayList<Recipe> recipes) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Recipes.txt",false));
            StringBuilder sb = new StringBuilder();
            for(Recipe x : recipes) {
                sb.append(x.getName());
                for(Ingridient i : x.getIngridients()) {
                    sb.append("|" + i.getName());
                }
                sb.append("\n");
            }
            bufferedWriter.append(sb.toString());
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRecipes() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Recipes.txt"));
            String data;
            while((data = bufferedReader.readLine()) != null){
                String[] vars = data.split("[|]");
                ArrayList<Ingridient> tmp = new ArrayList<>();
                for(int i = 1 ; i < vars.length ; i++) {
                    tmp.add(Recipe.searchIngidientByName(vars[i]));
                }
                Recipe recTmp = new Recipe(tmp,vars[0]);
                Main.addRecipe(recTmp);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
