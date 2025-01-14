package application;

import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Skeleton code for a terminal based calculator that reads an expression from the user and
 * evaluates it and prints out the answer.
 */
public class AsciiView implements ViewInterface {
  // The current question that the calculator must solve: entered like ?3*(5+4)
  private String question ="";

  // This method will be injected so we can ask the controller to calculate
  Runnable doCalculation = null;

  // This method changes how the calculator will evaluate the question
  Consumer<OpType> setCalculatorType = null;

  private void menu() {
    Scanner s = new Scanner(System.in);
    boolean finished = false;
    help();

    while (!finished && s.hasNext()) {
      String t = s.nextLine();
      switch (t.toUpperCase().charAt(0)) {
        case 'C': // Ask the controller to calculate
            if (doCalculation != null) {
                doCalculation.run();
            }
          break;
        case '?': // Set current question
            question = t.substring(1);
          break;
        case 'Q':
          System.out.println("Bye");
          finished = true;
          break;
        case 'S':
            if (setCalculatorType != null) {
                setCalculatorType.accept(OpType.STANDARD);
            }
            break;
        case 'R':
            if (setCalculatorType != null) {
                setCalculatorType.accept(OpType.REV_POLISH);
            }
            break;
        default:
          help();
      }
    }
    s.close();
  }

  private void help() {
    System.out.println("Use one of the following:");
    System.out.println("  ?Expression - to set expression");
    System.out.println("  C - to calculate");
    System.out.println("  S - change to a standard calculator");
    System.out.println("  R - change to a reverse polish calculator");
    System.out.println("  Q - to exit");
  }

  @Override
  public String getExpression() {
    return question;
  }

  @Override
  public void setAnswer(String answer) {
    System.out.println("Answer is " + answer);
  }

  @Override
  public void addCalculateObserver(Runnable f) {
    doCalculation = f;
  }

  @Override
  public void addTypeObserver(Consumer<OpType> c) {
    setCalculatorType = c;
  }

  @Override
  public void startView() {
    menu();
  }

}
