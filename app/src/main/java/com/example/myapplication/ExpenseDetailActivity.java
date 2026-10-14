package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.model.Expense;
import com.example.myapplication.utils.CurrencyUtils;

public class ExpenseDetailActivity extends AppCompatActivity {

    private int expensePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvAmount = findViewById(R.id.tvDetailAmount);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDate = findViewById(R.id.tvDetailDate);
        TextView tvNote = findViewById(R.id.tvDetailNote);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        Expense expense = (Expense) getIntent().getSerializableExtra("expense_item");
        expensePosition = getIntent().getIntExtra("expense_position", -1);

        if (expense != null) {
            tvTitle.setText(expense.getTitle());
            tvAmount.setText(CurrencyUtils.formatVnd(expense.getAmount()));
            tvCategory.setText(expense.getCategory());
            tvDate.setText(expense.getDate());
            tvNote.setText(expense.getNote());
        }

        btnBack.setOnClickListener(v -> finish());

        btnDeleteExpense.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("delete_position", expensePosition);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
