package onbon.bx06.mobiledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.awt.Color;
import java.awt.Font;

import onbon.bx06.Bx6GScreenClient;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.area.TextCaptionBxArea;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.utils.DisplayStyleFactory;
import onbon.bx06.utils.TextBinary;

public class MainActivity extends AppCompatActivity {

    private Bx6GScreenClient screen;

    private EditText outputText;

    private Button connButton;

    private Button disconnButton;

    private ToggleButton screenButton;

    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.outputText = (EditText)findViewById(R.id.outputText);
        this.connButton = (Button)findViewById(R.id.connButton);
        this.connButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        connect();
                    }
                }).start();
            }
        });

        this.disconnButton = (Button)findViewById(R.id.disconnButton);
        this.disconnButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        disconnect();
                    }
                }).start();
            }
        });

        this.screenButton = (ToggleButton)findViewById(R.id.screenButton);
        this.screenButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        turnOnOff();
                    }
                }).start();
            }
        });

        this.sendButton = (Button)findViewById(R.id.sendButton);
        this.sendButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        writeProgram();
                    }
                }).start();
            }
        });

        this.screen = new Bx6GScreenClient("MyScreen");
    }

    private boolean testDeployOKorNot() {
        try {
            int size = onbon.db.Factory.getIstance().fineSeriesList().size();
            this.connButton.setTextColor(Color.blue._color());
            this.connButton.setText("OK. Series Size:" + size);
            return true;
        }
        catch (Exception ex) {
            this.connButton.setText("Failed");
            this.outputText.setText(ex.getMessage());
            return false;
        }
    }

    private void connect() {
        final TextView addrText = (TextView)findViewById(R.id.addrText);
        final TextView portText = (TextView)findViewById(R.id.portText);
        if (screen.connect("" + addrText.getText(), Integer.parseInt("" + portText.getText()))) {
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.connButton.setEnabled(false);
                    MainActivity.this.disconnButton.setEnabled(true);
                    MainActivity.this.sendButton.setEnabled(true);
                    MainActivity.this.screenButton.setEnabled(true);
                    MainActivity.this.outputText.setText("CONN success");
                }
            });
        }
        else {
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("CONN failure");
                }
            });
        }
    }

    private void disconnect() {
        this.screen.disconnect();
        runOnUiThread(new Runnable() {
            public void run() {
                MainActivity.this.connButton.setEnabled(true);
                MainActivity.this.disconnButton.setEnabled(false);
                MainActivity.this.sendButton.setEnabled(false);
                MainActivity.this.screenButton.setEnabled(false);
            }
        });
    }

    private void turnOnOff(){
        if(this.screenButton.isChecked()) {
            MainActivity.this.screen.turnOn();
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("Turn ON");
                }
            });
        }
        else {
            this.screen.turnOff();
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("Turn OFF");
                }
            });
        }
    }

    private void writeProgram() {
        EditText msgText = (EditText)findViewById(R.id.msgText);
        try {
            Bx6GScreenProfile profile = screen.getProfile();
            DisplayStyleFactory.DisplayStyle[] styles = DisplayStyleFactory.getStyles().toArray(new DisplayStyleFactory.DisplayStyle[0]);

            ProgramBxFile p002 = new ProgramBxFile(2, profile);
            p002.setFrameShow(true);
            p002.setFrameSpeed(20);
            p002.loadFrameImage(3);

            TextCaptionBxArea textArea = new TextCaptionBxArea(96, 0, 32, 32, profile);
            textArea.setFrameShow(true);
            textArea.loadFrameImage(4);

            TextBxPage page = new TextBxPage("" + msgText.getText());
            // 对文本的处理是否自动换行
            page.setLineBreak(true);
            // 设置文本水平对齐方式
            page.setHorizontalAlignment(TextBinary.Alignment.NEAR);
            // 设置文本垂直居中方式
            page.setVerticalAlignment(TextBinary.Alignment.CENTER);
            // 设置文本字体
            page.setFont(new Font("Arial", Font.PLAIN, 12));         // 字体
            // 设置文本颜色
            page.setForeground(Color.red);
            // 设置区域背景色，默认为黑色
            page.setBackground(Color.gray);
            // 调整特技方式
            page.setDisplayStyle(styles[4]);
            // 调整特技速度
            page.setSpeed(2);
            // 调整停留时间, 单位 10ms
            page.setStayTime(0);

            textArea.addPage(page);

            p002.addArea(textArea);

            screen.writeProgramQuickly(p002);
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("WRITE OK");
                }
            });

        }
        catch (final Exception ex) {
            runOnUiThread(new Runnable() {
                public void run() {
                    MainActivity.this.outputText.setText("WRITE failure");
                }
            });
        }
    }}
