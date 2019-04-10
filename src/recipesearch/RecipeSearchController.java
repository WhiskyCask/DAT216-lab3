
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Ingredient;
import se.chalmers.ait.dat215.lab2.Recipe;

public class RecipeSearchController implements Initializable {

    private RecipeBackendController rbc = new RecipeBackendController();
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<>();

    @FXML private ComboBox mainIngredientComboBox;
    @FXML private ComboBox cuisineComboBox;
    @FXML private RadioButton radioButtonAll;
    @FXML private RadioButton radioButtonEasy;
    @FXML private RadioButton radioButtonMedium;
    @FXML private RadioButton radioButtonHard;
    @FXML private Spinner maxPriceSpinner;
    @FXML private Slider maxTimeSlider;
    @FXML private FlowPane displayRecipe;

    @FXML private ImageView recipeImageView;
    @FXML private Label recipeLabel;
    @FXML private ImageView imgCuisine;
    @FXML private ImageView imgMainIngredient;
    @FXML private ImageView imgDifficulty;

    @FXML private AnchorPane detailPane;
    @FXML private SplitPane searchPane;

    @FXML private ImageView closeImageView;

    @FXML private Label timeLabel;
    @FXML private Label priceLabel;
    @FXML private Label servingsLabel;
    @FXML private TextArea descriptionTextArea;
    @FXML private TextArea instructionTextArea;
    @FXML private TextArea ingredientsTextArea;

    @FXML private Label maxTimeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        rbc = new RecipeBackendController();

        /* Fill the map with recipes */
        for (Recipe recipe : rbc.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }

        updateRecipeList();

        mainIngredientComboBox.getItems().addAll("Visa alla", "Kött", "Fisk", "Kyckling", "Vegetarisk");
        mainIngredientComboBox.getSelectionModel().select("Visa alla");
        mainIngredientComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                rbc.setMainIngredient(newValue);
                updateRecipeList();
            }
        });

        cuisineComboBox.getItems().addAll("Visa alla", "Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike");
        cuisineComboBox.getSelectionModel().select("Visa alla");
        cuisineComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                rbc.setCuisine(newValue);
                updateRecipeList();
            }
        });

        ToggleGroup difficultyToggleGroup = new ToggleGroup();

        radioButtonAll.setToggleGroup(difficultyToggleGroup);
        radioButtonEasy.setToggleGroup(difficultyToggleGroup);
        radioButtonMedium.setToggleGroup(difficultyToggleGroup);
        radioButtonHard.setToggleGroup(difficultyToggleGroup);
        radioButtonAll.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    rbc.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200000, 0, 1);

        maxPriceSpinner.setValueFactory(valueFactory);
        maxPriceSpinner.valueProperty().addListener(new ChangeListener<Integer>() {

            @Override
            public void changed(ObservableValue observable, Integer oldValue, Integer newValue) {
                rbc.setMaxPrice(newValue);
                updateRecipeList();
            }
        });

        maxPriceSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    int value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                    rbc.setMaxPrice(value);
                    updateRecipeList();
                }
            }
        });

        maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue != null && !newValue.equals(oldValue) && !maxTimeSlider.isValueChanging()) {
                    rbc.setMaxTime(newValue.intValue());
                    maxTimeLabel.setText(newValue.toString());
                    updateRecipeList();
                }
            }
        });

        populateMainIngredientComboBox();
        populateCuisineComboBox();
    }

    private void updateRecipeList() {

        displayRecipe.getChildren().clear();
        RecipeListItem recipeListItem;

        for (Recipe recipe : rbc.getRecipes()) {

            recipeListItem = recipeListItemMap.get(recipe.getName());

            displayRecipe.getChildren().add(recipeListItem);

        }
    }

    private void populateRecipeDetailView(Recipe recipe) {
        recipeLabel.setText(recipe.getName());
        recipeImageView.setImage(getSquareImage(recipe.getFXImage()));
        imgCuisine.setImage(getCuisineImage(recipe.getCuisine()));
        imgMainIngredient.setImage(getMainIngredientImage(recipe.getMainIngredient()));
        imgDifficulty.setImage(getDifficultyImage(recipe.getDifficulty()));
        priceLabel.setText(Integer.toString(recipe.getPrice()));
        timeLabel.setText(Integer.toString(recipe.getTime()));
        servingsLabel.setText(Integer.toString(recipe.getServings()));
        descriptionTextArea.setText(recipe.getDescription());
        instructionTextArea.setText(recipe.getInstruction());

        StringBuilder ingredients = new StringBuilder("");
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients.append(ingredient);
            ingredients.append('\n');
        }
        ingredientsTextArea.setText(ingredients.toString());


    }

    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch(cuisine) {
            case "Sverige":
                iconPath = "recipesearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "recipesearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "recipesearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "recipesearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "recipesearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "recipesearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));

        }
        return null;
    }

    public Image getMainIngredientImage(String mainIngredient) {
        String iconPath;
        switch(mainIngredient) {
            case "Kött":
                iconPath = "recipesearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "recipesearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "recipesearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetarisk":
                iconPath = "recipesearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null;
    }

    public Image getDifficultyImage(String difficulty){
        String iconPath;
        switch(difficulty){
            case "Lätt":
                iconPath = "recipesearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "recipesearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "recipesearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null;
    }
    public Image getSquareImage(Image image) {

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if (image.getWidth() > image.getHeight()) {
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if (image.getHeight() > image.getWidth()) {
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }


    @FXML
    public void closeRecipeView() {
        searchPane.toFront();
    }

    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        detailPane.toFront();
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "recipesearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "recipesearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "recipesearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "recipesearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredientComboBox.setButtonCell(cellFactory.call(null));
        mainIngredientComboBox.setCellFactory(cellFactory);
    }

    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "recipesearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "recipesearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "recipesearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "recipesearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "recipesearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "recipesearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineComboBox.setButtonCell(cellFactory.call(null));
        cuisineComboBox.setCellFactory(cellFactory);
    }

    @FXML
    public void closeButtonMouseEntered(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "recipesearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMouseClicked(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "recipesearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "recipesearch/resources/icon_close.png")));
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }
}