package application;

import javafx.scene.control.Alert;

/**
 * The controller that sits between the calculator model that does actual evaluation and the view
 * that is the part the user interfaces with.
 */
public class CalcController {
    private CalcModel myModel;
    private ViewInterface myView;

    private boolean isInfix = false;

    private void handleCalculation() {
        System.out.println("calculation requested");
        System.out.println(myView.getExpression());
        try {
            float result = myModel.evaluate(myView.getExpression(), isInfix);

            // show 2 points of float
            String resultStr = String.format("%.2f", result);
            myView.setAnswer(resultStr);

        } catch (InvalidExpression e) {
          displayError("Invalid Expression", "Entered expression is not valid.");
        } catch (UnsupportedOperationException e) {
          displayError("Unsupported Operation", e.getMessage());
        }
    }

    // show Alert for error
  private static void displayError(String exceptionName, String contentText) {
      System.out.println("Error: " + exceptionName);
      System.out.println(contentText);
//    Alert alert = new Alert(Alert.AlertType.ERROR);
//    alert.setTitle("Error");
//    alert.setHeaderText(Invalid_Expression);
//    alert.setContentText(contentText);
//    alert.showAndWait();
  }

  private void handleTypeChange(OpType opType) {
        isInfix = opType == OpType.STANDARD;
        System.out.println("type changed " + opType + " " + isInfix);
    }

    CalcController(CalcModel model, ViewInterface view) {
        myModel = model;
        myView = view;

        myView.addCalculateObserver(this::handleCalculation);
        myView.addTypeObserver(this::handleTypeChange);
    }
}
