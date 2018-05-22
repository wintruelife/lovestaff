package love.wintrue.com.lovestaff.widget;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by dell on 2016/11/9.
 */
public class MyEditText extends EditText {
    private int maxNum = Integer.MAX_VALUE;
    private int minNum = 0;
    private OnTextChangeListener onTextChangeListener;

    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMaxMinNum(int maxNum,int minNum){
        this.maxNum = maxNum;
        this.minNum = minNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public int getMinNum() {
        return minNum;
    }

    public void setOnTextChangeListener(OnTextChangeListener listener){
        this.onTextChangeListener = listener;
    }

    public void setMyText(CharSequence text){
        isTextChanged = false;
        super.setText(text);
    }

    private boolean isTextChanged = true;
    private void init(){
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!isTextChanged){
                    isTextChanged = true;
                    return;
                }
                int count = minNum;
                try {
                    String numStr = getText().toString().trim();
                    count = TextUtils.isEmpty(numStr)?0:Integer.valueOf(numStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                count = count>maxNum?maxNum:count;
                count = count<minNum?minNum:count;
                isTextChanged = false;
                setText(count+"");
                Editable etext = getText();
                Selection.setSelection(etext, etext.length());
                if(onTextChangeListener != null){
                    onTextChangeListener.onTextChange(count);
                }
            }
        });
    }

    public interface OnTextChangeListener{
        void onTextChange(int count);
    }

}
