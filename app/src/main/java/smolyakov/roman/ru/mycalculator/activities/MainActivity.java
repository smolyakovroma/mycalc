package smolyakov.roman.ru.mycalculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.EnumMap;

import smolyakov.roman.ru.mycalculator.calculations.CalcOperations;
import smolyakov.roman.ru.mycalculator.enums.ActionType;
import smolyakov.roman.ru.mycalculator.enums.OperationType;
import smolyakov.roman.ru.mycalculator.R;
import smolyakov.roman.ru.mycalculator.enums.Symbol;
import smolyakov.roman.ru.mycalculator.exceptions.DivisionByZeroException;

public class MainActivity extends AppCompatActivity {

    private TextView txtResult;

    private Button btnAdd, btnSubstract, btnDivide, btnMultiply;

    private EnumMap<Symbol, Object> commands = new EnumMap<Symbol, Object>(Symbol.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView) findViewById(R.id.txtResult);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSubstract = (Button) findViewById(R.id.btnSubstract);
        btnMultiply = (Button) findViewById(R.id.btnMultiply);
        btnDivide = (Button) findViewById(R.id.btnDivide);

        btnAdd.setTag(OperationType.ADD);
        btnSubstract.setTag(OperationType.SUBSTRACT);
        btnDivide.setTag(OperationType.DIVIDE);
        btnMultiply.setTag(OperationType.MULTIPLY);

    }

    private OperationType operType;
    private ActionType lastAction;

    public void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubstract:
            case R.id.btnAdd:
            case R.id.btnMultiply:
            case R.id.btnDivide: {
                operType = (OperationType) v.getTag();

                if(lastAction==ActionType.OPERATION){
                    commands.put(Symbol.OPERATION, operType);
                    return;
                }
                if (!commands.containsKey(Symbol.OPERATION)) {
                    if (!commands.containsKey(Symbol.FIRST_DIGIT)) {
                        commands.put(Symbol.FIRST_DIGIT, txtResult.getText());
                    }
                    commands.put(Symbol.OPERATION, operType);
                } else if (!commands.containsKey(Symbol.SECOND_DIGIT)) {
                    commands.put(Symbol.SECOND_DIGIT, txtResult.getText());
                    doCalc();
                    commands.put(Symbol.OPERATION, operType);
                    commands.remove(Symbol.SECOND_DIGIT);
                }
            }
            lastAction = ActionType.OPERATION;
            break;
            case R.id.btnEqual:
                if (lastAction==ActionType.CALC) return;

                if (commands.containsKey(Symbol.FIRST_DIGIT) && commands.containsKey(Symbol.OPERATION)) {
                    commands.put(Symbol.SECOND_DIGIT, txtResult.getText());
                    doCalc();
                    commands.clear();
                }
                lastAction = ActionType.CALC;
                break;
            case R.id.btnClear:
                txtResult.setText("");
                commands.clear();
                lastAction = ActionType.CLEAR;
                break;
            case R.id.btnErase:
                txtResult.setText(txtResult.getText().toString().substring(0, txtResult.getText().length() - 1));
                if (txtResult.getText().toString().trim().length() == 0) {
                    txtResult.setText("0");
                }
                lastAction = ActionType.DELETE;
                break;
            case R.id.btnComma:
                if (commands.containsKey(Symbol.FIRST_DIGIT) && getDouble(txtResult.getText().toString()) == getDouble(commands.get(Symbol.FIRST_DIGIT).toString())) {
                    txtResult.setText("0" + v.getContentDescription().toString());
                }

                if (!txtResult.getText().toString().contains(".")) {
                    txtResult.setText(txtResult.getText() + ".");
                }
                lastAction = ActionType.COMMA;
                break;
            default:
                if (txtResult.getText().toString().equals("0") ||
                        (commands.containsKey(Symbol.FIRST_DIGIT) && getDouble(txtResult.getText()) == getDouble(commands.get(Symbol.FIRST_DIGIT))) ||
                        (lastAction==ActionType.CALC)) {
                    txtResult.setText(v.getContentDescription().toString());
                } else {
                    txtResult.setText(txtResult.getText() + v.getContentDescription().toString());
                }
                lastAction = ActionType.DIGIT;
        }
    }

    private void doCalc() {
        OperationType operType2 = (OperationType) commands.get(Symbol.OPERATION);

        double result = 0;
        try {
            result = calc(operType2, getDouble(commands.get(Symbol.FIRST_DIGIT)), getDouble(commands.get(Symbol.SECOND_DIGIT)));
        } catch (DivisionByZeroException e) {
            showToastMessage(R.string.division_zero);
            return;
        }

        if (result % 1 == 0) {
            txtResult.setText(String.valueOf((int) result));
        } else {
            txtResult.setText(String.valueOf(result));
        }
        commands.put(Symbol.FIRST_DIGIT, result);
    }

    private void showToastMessage(int division_zero) {
        Toast toast = Toast.makeText(this, division_zero, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 100);
        toast.show();
    }

    private double getDouble(Object o) {
        double result = 0;

        try {
            result = Double.valueOf(o.toString().replace(',', '.')).doubleValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = 0;
        }

        return result;
    }

    private Double calc(OperationType operType, double a, double b) {
        switch (operType) {
            case ADD:
                return CalcOperations.add(a, b);
            case DIVIDE:
                return CalcOperations.divide(a, b);
            case MULTIPLY:
                return CalcOperations.multiply(a, b);
            case SUBSTRACT:
                return CalcOperations.substract(a, b);
        }
        return null;
    }
}
























