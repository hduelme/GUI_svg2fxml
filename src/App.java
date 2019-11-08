package SVG_TO_FXML;


import java.io.File;
import java.nio.file.*;; 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.util.regex.Pattern;
import javafx.scene.control.Label;

public class SVG_TO_FXML extends Application
{
    private FileChooser svgFCH, fxmlFCH, toolFCH;
    private TextField svgTF, fxmlTF;
    private Button btn1,btn2,btn3,btn4;
    private String nazov_pre_save, cesta_tool;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("SVG2FXML");

        initKomponenty();

        GridPane GL = new GridPane();

        GL.setVgap(10);
        GL.setHgap(2);

        GL.add(svgTF, 0, 0);
        GL.add(btn1, 1,0);
        GL.add(fxmlTF, 0, 1);
        GL.add(btn2, 1,1);
         

        GL.add(btn3, 0, 2);
        GL.add(btn4, 1,2);
        GL.add(new Label("Origin made by: D.Kolibár"), 0,3);
        GL.add(new Label("Modified by: hduelme (2019)"), 0,4);
        primaryStage.setScene(new Scene(GL));

        primaryStage.setResizable(false);
        primaryStage.sizeToScene();

        primaryStage.show();

        btn1.setOnAction(event ->
        {
            String cesta = "";

            try
            {
                cesta = svgFCH.showOpenDialog(primaryStage).getAbsolutePath();
            }
            catch (Exception err)
            {
                System.out.println(err);
            }

            if(!cesta.isEmpty())
            {
                String separator = "\\";
                String exploded[] = cesta.split(Pattern.quote(separator));

                nazov_pre_save = exploded[exploded.length - 1];

                String exploded2[] = nazov_pre_save.split("\\.");
                nazov_pre_save = exploded2[0];

                btn2.setDisable(false);

                svgTF.setText(cesta);
            }
        });

        btn2.setOnAction(event ->
        {
            String cesta = "";

            if(!nazov_pre_save.isEmpty())
            {
                fxmlFCH.setInitialFileName(nazov_pre_save);
                try
                {
                    cesta = fxmlFCH.showSaveDialog(primaryStage).getAbsolutePath();
                }
                catch (Exception err)
                {
                    System.out.println(err);
                }
            }

            if(!cesta.isEmpty())
            {
                fxmlTF.setText(cesta);
                btn3.setDisable(false);
            }
        });

        btn3.setOnAction(event ->
        {
            if(cesta_tool == null)
            {
                File f = new File("svg2fxml-0.8.1-SNAPSHOT.jar");
                if(f.exists())
                {
                    cesta_tool = f.getAbsolutePath();
                }
                else
                {
                    cesta_tool = toolFCH.showOpenDialog(primaryStage).getAbsolutePath();
                    doMagic();
                }
                
            }
            else
                doMagic();
        });
        
        btn4.setOnAction(event ->
        {
            if(!fxmlTF.getText().equals("") || !fxmlTF.getText().equals("Please select")){
                fixFile();
            }
            else{
                btn2.setDisable(false);
                fxmlTF.setText("Please select");
            }
        });
    }

    private void doMagic()
    {
        try
        {
            String command = "java -jar \"" + cesta_tool + "\" \"" + svgTF.getText() + "\" \"" + fxmlTF.getText() + "\"";
            System.out.println(command);
            Process proc = Runtime.getRuntime().exec(command);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

        
            

    }

    private void initKomponenty()
    {
        //file choosre
        svgFCH = new FileChooser();
        svgFCH.getExtensionFilters().add(new FileChooser.ExtensionFilter("SVG", "*.svg"));
        svgFCH.setTitle("Select your SVG file...");

        fxmlFCH = new FileChooser();
        fxmlFCH.getExtensionFilters().add(new FileChooser.ExtensionFilter("FXML", "*.fxml"));
        fxmlFCH.setTitle("Choose where FXML will be saved...");

        toolFCH = new FileChooser();
        toolFCH.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR", "*.jar"));
        toolFCH.setTitle("Locate the SVG2FXML.jar tool by e(fx)clipse.org community!");
        //

        Font font = new Font(25);

        svgTF = new TextField();
        svgTF.setEditable(false);
        svgTF.setPrefWidth(350);
        svgTF.setFocusTraversable(false);

        btn1 = new Button("SVG...");
        btn1.setFont(font);

        fxmlTF = new TextField();
        fxmlTF.setEditable(false);
        fxmlTF.setPrefWidth(350);
        fxmlTF.setFocusTraversable(false);

        btn2 = new Button("...FXML");
        btn2.setDisable(true);
        btn2.setFont(font);

        btn3 = new Button("SVG2FXML!");
        btn3.setDisable(true);
        btn3.setFont(font);
        
        btn4 = new Button("Repair");
        btn4.setFont(font);
    }
    
    private void fixFile()
    {
       List<String> lines = null;
        System.out.println("Read:" +fxmlTF.getText());
        try
        { 
            lines = 
            Files.readAllLines(Paths.get(fxmlTF.getText()), StandardCharsets.UTF_8); 
            for(int x = 0; x < lines.size();x++)
            {
                if(lines.get(x).contains("linearGradient") || lines.get(x).contains("radialGradient"))
                {
                    System.out.println("delte"+x);
                    lines.remove(x);
                }
                //System.out.println(x);
            }
            Files.write(Paths.get(fxmlTF.getText()),lines, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.out.println(ex);
        } 
    }


    

    public static void main(String[] args) {
        launch(args);
    }
}