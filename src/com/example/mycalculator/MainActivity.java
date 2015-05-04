package com.example.mycalculator;

import android.R.bool;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{

	EditText myText;
	Button bckSpace;
	ExpressionProcess ep = new ExpressionProcess();
	OnTouchListener ot1 = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
		      case MotionEvent.ACTION_DOWN:
		          Layout layout = ((EditText) v).getLayout();
		          float x = event.getX() + myText.getScrollX();
		          int offset = layout.getOffsetForHorizontal(0, x);
		          if(offset>0)
		              if(x>layout.getLineMax(0))
		                  myText.setSelection(offset);     // touch was at end of text
		              else
		                  myText.setSelection(offset - 1);
		          break;
		    }
			int x = ((EditText) v).getSelectionStart();
			Log.d("vaman", "click position "+x);
			return true;
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        myText = (EditText)findViewById(R.id.editText1);
        bckSpace = (Button)findViewById(R.id.buttonbksp);
		//        bckSpace.setOnClickListener(this);
        myText.setOnTouchListener(ot1);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    
   @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	// TODO Auto-generated method stub
    	super.onConfigurationChanged(newConfig);
    	Log.d("vaman", "orientation changed");
    	//setContentView(R.layout.activity_main);
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId() == R.id.buttonbksp){
			Log.d("vaman", "backspace is clicked");
			removeLastChar();
		}
		if(v.getId() == R.id.buttonAC){
			Log.d("vaman", "AC  is clicked");
			removeLastChar();
		}

	}
    public void appendText(View v){
    	String str = (String) ((Button ) v).getText() ;
    	int x = myText.getSelectionStart();
    	Log.d("vaman", "value of index"+x);
    	myText.getText().insert(x, str);
    }
    
    public boolean isPresededByMinus(){
    	int index = myText.getSelectionStart();
		if(myText.getText().charAt(index-1) == '-'){
			return true;
		}
		return false;
    }
    
    public boolean isPresededByOp(){
    	int index = myText.getSelectionStart();
    	char ch = myText.getText().charAt(index-1);
    	if( ch == '-' || ch == '+' || ch == 'x' || ch == '/'){
    		return true;
    	}
    	return false;
    }
    public void appendOperator(View v){
    	if(myText.getSelectionStart()<1)
    		return;
    	String str = (String) ((Button) v).getText();
    	if(str.equals("-")){
    		if(!isPresededByMinus()){
    			appendText(v);
    		}
    	}
    	else{
    		while(isPresededByOp()){
    			removeLastChar();
    		}
			appendText(v);
    	}
    }
    public void clearContent(View v){
    	myText.setText("");
    	
    }
    
    public void modifyContent(View v) {
		// TODO Auto-generated method stub
		
		if( ( (Button)v ).getId() == R.id.buttonbksp){
			Log.d("vaman", "backspace is clicked");
			removeLastChar();
		}

	}
	
	public void removeLastChar(){
		//int index = myText.getSelectionStart();
		int end = myText.getSelectionEnd();
		SpannableStringBuilder selectedStr=new SpannableStringBuilder(myText.getText());
		if(end>0){
			//getting the selected Text
			//replacing the selected text with empty String and setting it to EditText
			selectedStr.replace(end-1, end, "");
			myText.setText(selectedStr);
			myText.setSelection(end-1);
		}
		else{
			Log.d("vaman", "in else aprt "+end);
			int length = myText.length();
			if(length>0){
				selectedStr.replace(length-1, length, "");
				myText.setText(selectedStr);
				myText.setSelection(length-1);
			}
			
		}
		
	}
	
	public void findResult(View v){
		String str = (String) myText.getText().toString();
		Log.d("vaman", "expresstion to evaluate "+str);
		Float result = new Float(0);
		boolean status = ep.process(str, result);
		Log.d("vaman", "result of the exp status"+ status + " value " +result);
	}

}
