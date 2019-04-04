
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;

public class RecipeSearchController implements Initializable {

    private RecipeBackendController rbc = new RecipeBackendController();

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

    @FXML private AnchorPane detailPane;
    @FXML private SplitPane searchPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        rbc = new RecipeBackendController();
        updateRecipeList();

        mainIngredientComboBox.getItems().addAll("Visa alla", "Apa", "Bepa", "Cepa", "Depa");
        mainIngredientComboBox.getSelectionModel().select("Visa alla");
        mainIngredientComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                rbc.setMainIngredient(newValue);
                updateRecipeList();
            }
        });

        cuisineComboBox.getItems().addAll("Visa alla", "Sverige", "Frankrike", "Cepa", "Depa");
        cuisineComboBox.getSelectionModel().select("Visa alla");
        cuisineComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
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
                }
            }
        });
    }

    private void updateRecipeList() {

        displayRecipe.getChildren().clear();
        RecipeListItem recipeListItem;

        for (Recipe recipe : rbc.getRecipes()) {

            recipeListItem = new RecipeListItem(recipe, this);

            displayRecipe.getChildren().add(recipeListItem);

        }
    }

    private void populateRecipeDetailView(Recipe recipe) {
        recipeLabel.setText(recipe.getName());
        recipeImageView.setImage(recipe.getFXImage());
    }

    @FXML
    public void closeRecipeView() {
        searchPane.toFront();
    }

    public void openRecipeView(Recipe recipe) {
        populateRecipeDetailView(recipe);
        detailPane.toFront();
    }

}