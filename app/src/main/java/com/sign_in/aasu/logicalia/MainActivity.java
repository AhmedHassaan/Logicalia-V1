package com.sign_in.aasu.logicalia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    //stack hol operators
    Stack<Character> chars;
    // textView hold the data
    TextSwitcher textView;
    String result = "";
    String statement = "";
    int open = 0,close = 0;
    HistoryData historyData;
    ImageButton delete;

    //get Precedence from operators
    private static int getPrecedence(char a) {
        switch (a) {
            case ')':
                return 8;
            case '(':
                return 7;
            case '¬':
                return 6;
            case '⊕':
                return 5;
            case '∧':
                return 4;
            case '∨':
                return 3;
            case '→':
                return 2;
            case '↔':
                return 1;
            default:
                return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        chars = new Stack<>();
        historyData = new HistoryData(this);
        textView = (TextSwitcher) findViewById(R.id.editText);
        textView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView text = new TextView(getApplication());
                text.setTextSize(25);
                text.setGravity(Gravity.CENTER_VERTICAL);
                text.setPadding(5, 5, 10, 5);
                text.setVerticalScrollBarEnabled(true);

                text.setMovementMethod(new ScrollingMovementMethod());
                text.setTextColor(Color.WHITE);
                return text;
            }
        });
        delete = (ImageButton) findViewById(R.id.button13);
        // delete last char
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!statement.isEmpty()) {
                    char prev = statement.charAt(statement.length() - 1);
                    if(prev == '(')
                        open--;
                    else if(prev == ')')
                        close--;
                    statement = statement.substring(0, statement.length() - 1);
                    textView.setText(statement);
                }
            }
        });
        // delete all the character from textView
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                statement = "";
                textView.setText("");
                open = 0;close = 0;
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.history){
            Intent intent = new Intent(this,History.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void convert(View view) {
        if (!statement.equals("")) {
            char tempe = statement.charAt(statement.length()-1);
            if(is_operator(tempe) && tempe!=')'){
                makeToast("Error");
            }
            else {
                while(open>close){
                    statement += ')';
                    close++;
                }
                char[] tempStatement = statement.toCharArray();
                // for each character
                for (char temp : tempStatement) {
                    if (is_operator(temp)) {
                        // if the operation is closed bracket
                        if (getPrecedence(temp) == 8) {
                            while (chars.peek() != '(') {
                                result += chars.pop();
                            }
                            chars.pop();
                        }
                        // put operations in stack
                        else {
                            // if stack is empty don't make comparison
                            if (chars.isEmpty())
                                chars.push(temp);

                            else {
                                while (true) {
                                    // if operation prec less or equal the character and not equal open bracket
                                    if (getPrecedence(temp) <= getPrecedence(chars.peek()) && getPrecedence(chars.peek()) != 7) {
                                        result += chars.pop();
                                        if (chars.isEmpty()) {
                                            chars.push(temp);
                                            break;
                                        }
                                    } else {
                                        chars.push(temp);
                                        break;
                                    }

                                }

                            }
                        }
                    }
                    // if it isn't not operation put the char into the string
                    else
                        result += temp;

                }
                // put the operations in stack in result at the end
                while (!chars.isEmpty())
                    result += chars.pop();
                getTruthValue(result);
            }
        }
    }

    // get the button text to textview
    public void getChar(View view) {
        Button temp = (Button) view;
        boolean enter = true;
        char charWritten = temp.getText().toString().charAt(0);
        if(statement.length()>=1) {
            char prevChar = statement.charAt(statement.length() - 1);
            if(charWritten==')' && !(open>close)){
                makeToast("Error");
                enter = false;
            }
            if(prevChar == '(' && is_operator(charWritten) && charWritten!='(' && charWritten!='¬'){
                makeToast("Error");
                enter = false;
            }
            else if(prevChar == ')' && charWritten == '('){
                makeToast("Error");
                enter = false;
            }

            else if (is_operator(charWritten) && is_operator(prevChar) && charWritten != '¬' && charWritten!=')' && charWritten!='(' && prevChar!=')') {
                makeToast("Error");
                enter = false;
            }
            else if (!is_operator(charWritten) && !is_operator(prevChar)) {
                makeToast("Error");
                enter = false;
            }
            else if(charWritten == '(' && !is_operator(prevChar)){
                makeToast("Error");
                enter = false;
            }
            else if(!is_operator(charWritten) && prevChar==')'){
                makeToast("Error");
                enter = false;
            }
            else if(prevChar == ')' && charWritten == '¬'){
                makeToast("Error");
                enter = false;
            }
            else if(!is_operator(prevChar) && charWritten == '¬'){
                makeToast("Error");
                enter = false;
            }
        }
        else{
            if (is_operator(charWritten) && charWritten != '¬' && charWritten!= '(') {
                makeToast("Error");
                enter = false;
            }
        }
        if(enter) {
            if(charWritten == '(')
                open++;
            else if(charWritten == ')')
                close++;
            statement += charWritten;
            textView.clearAnimation();
            textView.setText(statement);
        }
    }

    // check if the char is an operator
    private boolean is_operator(char a) {

        return (a == '∧') || (a == '∨') || (a == '¬') || (a == '→') || a == '↔' || a == '⊕' || (a == '(') || (a == ')');
    }

    // get truth value for the expression
    public void getTruthValue(String s) {
        Map<Character, Boolean> character = new HashMap<>();
        Random rand = new Random();
        ArrayList<Character> usedCharacter = new ArrayList<>();
        Stack<Boolean> answer = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (!character.containsKey(s.charAt(i)) && !is_operator(s.charAt(i))) {
                Boolean randomValue = rand.nextBoolean();
                character.put(s.charAt(i), randomValue);
                answer.push(randomValue);
                usedCharacter.add(s.charAt(i));
            } else if (character.containsKey(s.charAt(i)) && !is_operator(s.charAt(i)))
                answer.push(character.get(s.charAt(i)));
            if (is_operator(s.charAt(i))) {
                char a = s.charAt(i);
                Boolean second, first;
                switch (a) {
                    case '¬':
                        answer.push(!answer.pop());
                        break;
                    case '∧':
                        second = answer.pop();
                        first = answer.pop();
                        answer.push(first && second);
                        break;
                    case '∨':
                        second = answer.pop();
                        first = answer.pop();
                        answer.push(first || second);
                        break;
                    case '→':
                        second = answer.pop();
                        first = answer.pop();
                        if (first && !second)
                            answer.push(false);
                        else
                            answer.push(true);
                        break;
                    case '↔':
                        second = answer.pop();
                        first = answer.pop();
                        if ((first && second) || (!first && !second))
                            answer.push(true);
                        else
                            answer.push(false);
                        break;
                    case '⊕':
                        second = answer.pop();
                        first = answer.pop();
                        if (first != second)
                            answer.push(true);
                        else
                            answer.push(false);
                        break;
                }
            }
        }
        Collections.sort(usedCharacter);
        String assumingValue = "";
        for (int i = 0; i < usedCharacter.size(); i++) {
            if (i != usedCharacter.size() - 1)
                assumingValue += " " + usedCharacter.get(i) + " = " + character.get(usedCharacter.get(i)).toString() + " , ";
            else
                assumingValue += " " + usedCharacter.get(i) + " = " + character.get(usedCharacter.get(i)).toString() + " ";
        }
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        textView.setInAnimation(in);
        textView.setOutAnimation(out);
        String finalResult = "Infix = " + statement
                + "\n" + "PostFix = " + result
                + "\n" + "Let " + assumingValue
                + "\n" + "Truth Value is " + answer.pop();
        textView.setText(finalResult);
        historyData.putOne(finalResult);
        textView.clearAnimation();
        statement = "";
        character.clear();


        result = "";

    }

    public void makeToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}