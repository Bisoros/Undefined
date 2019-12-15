package com.e.hackatonapp;

import android.widget.ArrayAdapter;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAccountsList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] IBAN;
    private final String[] Amount;
    private final String[] Currency;

    public MyAccountsList(Activity context, String[] IBAN,String[] Amount, String[] Currency) {
        super(context, R.layout.myaccountlist, IBAN);

        this.context=context;
        this.IBAN=IBAN;
        this.Amount=Amount;
        this.Currency=Currency;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.myaccountlist, null,true);

        TextView IBANText =  rowView.findViewById(R.id.IBAN);
        TextView AmountText = rowView.findViewById(R.id.Amount);
        TextView CurrencyText = rowView.findViewById(R.id.Currency);

        IBANText.setText(IBAN[position]);
        AmountText.setText(Amount[position]);
        CurrencyText.setText(Currency[position]);

        return rowView;
    };
}
