package com.example.dingtaihw.ui.gallery;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.dingtaihw.Model.LL.SendParts;
import com.example.dingtaihw.R;

public class CustomTextWatcher implements TextWatcher {
    private EditText editText;
    private SendParts sendParts;

    public CustomTextWatcher(EditText e,SendParts sendParts)
    {this.editText=e;
        this.sendParts=sendParts;

    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
         String text=editable.toString();
         if(text!=null&&text.length()>0)
         {
             if(editText.getId()== R.id.pc)
             {
                 sendParts.setPno(text);
             } else if (editText.getId()==R.id.sendnum) {

                 sendParts.setSendnum(text);
             }
             else  if(editText.getId()==R.id.s_lh)
             {
                 sendParts.setLh(text);
             }
         }
    }
}
