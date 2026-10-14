package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.model.Expense;

import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etExpenseName, etExpenseAmount, etExpenseNote;
    private Spinner spinnerCategory;
    private DatePicker datePickerExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etExpenseName = findViewById(R.id.etExpenseName);
        etExpenseAmount = findViewById(R.id.etExpenseAmount);
        etExpenseNote = findViewById(R.id.etExpenseNote);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        datePickerExpense = findViewById(R.id.datePickerExpense);
        Button btnSaveExpense = findViewById(R.id.btnSaveExpense);

        String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSaveExpense.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String title = etExpenseName.getText().toString().trim();
        String amountStr = etExpenseAmount.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String note = etExpenseNote.getText().toString().trim();

        if (title.isEmpty()) {
            etExpenseName.setError(getString(R.string.error_empty_expense_name));
            etExpenseName.requestFocus();
            return;
        }

        long amount = 0;
        try {
            amount = Long.parseLong(amountStr);
        } catch (NumberFormatException e) {
            etExpenseAmount.setError(getString(R.string.error_invalid_amount));
            etExpenseAmount.requestFocus();
            return;
        }

        if (amount <= 0) {
            etExpenseAmount.setError(getString(R.string.error_invalid_amount));
            etExpenseAmount.requestFocus();
            return;
        }

        int day = datePickerExpense.getDayOfMonth();
        int month = datePickerExpense.getMonth() + 1;
        int year = datePickerExpense.getYear();
        String date = String.format("%02d/%02d/%d", day, month, year);

        if (note.isEmpty()) {
            note = title;
        }

        String id = UUID.randomUUID().toString();
        Expense newExpense = new Expense(id, title, amount, category, date, note);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_expense", newExpense);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
