package proje;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class SimpleHighlighterGUI extends JFrame {
	private JTextPane textPane;
    private StyledDocument doc;
    private Map<String, SimpleAttributeSet> styleMap;
    private JLabel parseStatusLabel;
    
    public SimpleHighlighterGUI() {
        setTitle("Gercek Zamanli Sozdizimi Renklendiricisi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        parseStatusLabel = new JLabel("Durum: Kod Giriniz:");
        parseStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        parseStatusLabel.setForeground(Color.DARK_GRAY);
        getContentPane().add(parseStatusLabel, BorderLayout.SOUTH);

        textPane = new JTextPane();
        textPane.setFont(new Font("Monospaced",Font.PLAIN,18));        
        doc = textPane.getStyledDocument();
        JScrollPane scrollPane = new JScrollPane(textPane);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        createStyles();
        addListeners();
        
        setVisible(true);
}

    
    private void createStyles() {
        styleMap = new HashMap<>();
        styleMap.put("VERI_TIPI", createStyle(Color.BLUE));
        styleMap.put("ANAHTAR KELIME", createStyle(new Color(138, 43, 226)));
        styleMap.put("DEGISKEN", createStyle(Color.BLACK));
        styleMap.put("SAYI", createStyle(new Color(0, 128, 0)));
        styleMap.put("OPERATOR", createStyle(Color.RED));
        styleMap.put("SEMBOL", createStyle(Color.GRAY));
        styleMap.put("STRING", createStyle(new Color(199, 21, 133)));
        styleMap.put("YORUM_SATIRI", createStyle(new Color(105, 105, 105)));
        styleMap.put("BILINMIYOR", createStyle(Color.ORANGE));
    }
    
    
    private SimpleAttributeSet createStyle(Color color) {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, color);
        return set;
    }
    
    
    private void addListeners() {
        textPane.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                SwingUtilities.invokeLater(() -> highlight());
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                SwingUtilities.invokeLater(() -> highlight());
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });
    }
    
    
    private void highlight() {
        String text = textPane.getText();
        doc.setCharacterAttributes(0, text.length(), createStyle(Color.BLACK), true);

        StringTokenizer st = new StringTokenizer(text, " +-*/=<>{}[]();,\\\"&\n\t", true);
        int offset = 0;

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (token.trim().isEmpty()) {
                offset += token.length();
                continue;
            }

            String type = SimpleTokenizer.detectTokenType(token);
            SimpleAttributeSet style = styleMap.getOrDefault(type, styleMap.get("BILINMIYOR"));
            doc.setCharacterAttributes(offset, token.length(), style, true);
            
            offset += token.length();
        }

        checkSyntax();
    }
    
    
    private void checkSyntax() {
        String code = textPane.getText();
        List<Token> tokenList = SimpleTokenizer.tokenize(code);
        
        System.out.println("--------Token Listesi----------");
        for(Token t:tokenList) {
        	System.out.println(t);
        }
        System.out.println("---------------------------------");
        
        Parse parser = new Parse(tokenList);

        if (parser.parse()) {
            parseStatusLabel.setText("Soz dizimi gecerli");
            parseStatusLabel.setForeground(new Color(0, 128, 0));
        } else {
            parseStatusLabel.setText("Soz dizimi gecersiz");
            parseStatusLabel.setForeground(Color.RED);
        }
    }
    
}
    
            
    