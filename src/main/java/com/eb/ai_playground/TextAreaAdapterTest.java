package com.eb.ai_playground;

import com.eb.base.gui.adapter.JTextAreaAdapter;

import javax.swing.*;

public class TextAreaAdapterTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TextAreaAdapter Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JTextArea textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));



            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            JTextAreaAdapter adapter = new JTextAreaAdapter(scrollPane, textArea);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

            textArea.setText(fullText);

            JButton lineNrButton = new JButton("Zeile unter Cursor");
            lineNrButton.addActionListener(e -> {
                int lineNr = adapter.getLineNrUnderCursor();
                JOptionPane.showMessageDialog(frame, "Zeilennummer: " + lineNr);
            });

            JButton visibleLinesButton = new JButton("Sichtbare Zeilen");
            visibleLinesButton.addActionListener(e -> {
                int visibleLines = adapter.getVisibleLinesCount();
                JOptionPane.showMessageDialog(frame, "Sichtbare Zeilen: " + visibleLines);
            });

            JButton firstLineButton = new JButton("Erste sichtbare Zeile");
            firstLineButton.addActionListener(e -> {
                int firstLine = adapter.getFirstVisibleLineNr();
                JOptionPane.showMessageDialog(frame, "Erste Zeile: " + firstLine);
            });

            JButton wordButton = new JButton("Wort unter Cursor");
            wordButton.addActionListener(e -> {
                String word = adapter.getWordUnderCursor();
                JOptionPane.showMessageDialog(frame, "Wort: \"" + word + "\"");
            });

            JButton sentenceButton = new JButton("Satz unter Cursor");
            sentenceButton.addActionListener(e -> {
                String sentence = adapter.getSentenceUnderCursor();
                JOptionPane.showMessageDialog(frame, "Satz: \"" + sentence + "\"");
            });

            JButton paragraphButton = new JButton("Absatz unter Cursor");
            paragraphButton.addActionListener(e -> {
                String paragraph = adapter.getParagraphUnderCursor();
                JOptionPane.showMessageDialog(frame, "Absatz: \"" + paragraph + "\"");
            });

            JButton setFirstLineButton = new JButton("Absatz unter Cursor");
            setFirstLineButton.addActionListener(e -> {
                int nr = adapter.getLineNrUnderCursor();
                adapter.setFirstVisibleLine(nr);
            });


            controlPanel.add(lineNrButton);
            controlPanel.add(visibleLinesButton);
            controlPanel.add(firstLineButton);
            controlPanel.add(wordButton);
            controlPanel.add(sentenceButton);
            controlPanel.add(paragraphButton);
            controlPanel.add(setFirstLineButton);

            frame.setLayout(new java.awt.BorderLayout());
            frame.add(scrollPane, java.awt.BorderLayout.CENTER);
            frame.add(controlPanel, java.awt.BorderLayout.EAST);
            frame.setVisible(true);
        });
    }

    static String fullText = """
            Birinci bölüm
            
            Yazgıya inanmam, ama olaylar bu düşüncemin yanlışlığını kanıtlamak istercesine ardı ardına sıralanmaya başladığında, bunları kurgulayan biri mi var, diye endişelenmekten de kendimi alamam.
            
            Geçtiğimiz güz de böyle olmuştu. Asla bir araya gelemeyecek kişiler buluşmuş, hiç ilgisi olmayan olaylar birbirine bağlanmış, konular iç içe geçmiş; böylece biz üç eski kafadar, Beyoğlu'nun o kederli sonbahar günlerinde tuhaf bir serüvenin sert rüzgârıyla savrulurken bulmuştuk kendimizi.
            
            Üç kafadar derken, bendeniz Selim, arkadaşlarım Kenan ve Nihat'ı kastediyorum. Yan yana dizilmiş üç erkek ismini görüp, arkadaş olduğumuzu da öğrenince, üstelik serüven lafını da okuyunca sakın aklınıza genç insanlar gelmesin. Gençliğin deli rüzgârları terk etmişti bizi. Hayır, ihtiyar da sayılmazdık, uzunca bir süredir orta yaşın çoktan kanıksadığımız sıradan günlerinin devranını sürmekteydik. Ta ki Kenan'ın ölümsüzlük merakı yüzünden bu sakin yaşamımız, fırtınalı günlerle örülü bir karabasana dönüşene kadar. Sakin yaşamımızın nasıl sona erdiğini uzun uzun anlatacağım, ama önce arkadaşlarımı tanıtayım sizlere.
            
            Orta yaşlarımızı sürüyorduk dedim ya, aslında arkadaşlığımız çok eskilere, kısa pantolonla dolaştığımız çocukluk günlerine kadar uzanır. Kenan ile Nihat'ı, Galatasaray Lisesi'nin Ortaköy'deki tarihî binasının geniş bahçesinde ilk gördüğümde üçümüz de henüz delikanlılığın sınırlarına bile gelmemiştik. Neden arkadaş olduğumuzu bilmiyorum. Aynı sınıfta olmanın doğal bir sonucu desem, onlarca çocuğun arasından neden üçünüz bir araya geldiniz, diyerek kolayca çürütülebilir bu tezim. Belki izcilik... Evet, üçümüz de okulun ünlü izci oymağına girmiştik, ama orada bizim gibi onlarca çocuk vardı. Cılız bedenlerimize geçirdiğimiz o güzelim üniformalar, el birliğiyle kurulan çadırlar, yakılan kamp ateşleri, bayram törenlerinde okuldan çıkarken cakalı başlayıp, akşam dönüşünde saatlerce ayakta kaldığımız için bozguna dönüşen yürüyüşler... Kuşkusuz bunlar bizi yakınlaştırmıştı, ama sanırım daha önemli bir olgu vardı. Hayır, hayır üçümüzün de ailelerimizin tek çocuğu olmamızdan söz etmiyorum, kişiliklerimizden bahsediyorum. Yanlış anlamayın, kişiliklerimiz de tıpkı dış görünüşümüz gibi birbirine hiç benzemezdi. Kıvırcık sayılabilecek dalgalı siyah saçları, hep neşeyle parıldayan ela gözleri, dur durak bilmeyen haliyle Kenan, içimizdeki en delişmen çocuktu.
            
            Nihat ise, iri bir yumurtayı andıran kafası, geniş alnının hemen altında insana kederle bakan kara gözleri, kısa boyu, çelimsiz bedeniyle ikimizden de çok farklıydı. Yine de tuhaf bir şekilde ikimize de benzerdi. Belki benzemezdi de, kimi davranışlarımızı taklit ederek bizim gibi olmaya çalışırdı.
            
            Bana gelince, uzun boyum, iri bedenim, yeşil mi, gri mi çoğu zaman benim bile ayırt edemediğim, ilgi çekmeyen açık renk gözlerim, şimdi iyice seyrekleşen, ince telli, kumral saçlarım, her zaman temiz, kırışıksız olmasına özen gösterdiğim giysilerim, kurallara harfiyen uyan davranışlarımla sıradan öğrencilerden biriydim. Tıpkı özenli giysilerim gibi, ağırbaşlılığım da o yıllardan bu yana taşıdığım bir özelliktir. Bu yüzden hep olduğumdan daha yaşlı görünürüm...
            
            Kişiliklerimiz diyordum; evet, okulun hazırlık sınıfında başlayıp yıllarca süren sağlam dostluğumuzun altında yatan asıl neden buydu galiba. Oldukça farklı olan kişiliklerimiz, yan yana geldiğimizde tamamlanıyor, bizi birbirimize çeken tuhaf bir ruhsal üçgen ortaya çıkıyordu... Ruhsal üçgen mi dedim? Kenan duysa, önce şaşırır, sonra bu üçgene esrarengiz anlamlar yüklemeye kalkışırdı.
            
            Şaka bir yana Kenan başından beri metafizik konulara ikimizden daha çok ilgi gösterirdi. Benim merakım polisiye romanlardı; Sherlock Holmes'un maceralarına, Arsene Lupin'in hırsızlıklarına, Hercule Poirot'nun karmaşık cinayetleri kolayca çözmesine bayılırdım; hâlâ da bayılırım. Evimdeki kütüphane polisiye romanlarla doludur. Oysa Kenan okulumuzun yaşlı kütüphanesinden hep korku romanlarını, hayalet, cadı, büyücülük konularını anlatan hikâyeleri seçerdi. Okumakla kalsa iyi, bu romanların tahrik ettiği hayal gücünü çalıştırarak, kahramanları üçümüzden oluşan ve Beyoğlu'nun yüzlerce yıllık binalarında yaşanan korku öyküleri anlatırdı. Beş yüz küsur yıldır bu yerde bulunan okulun bahçesine, ıssızlığın kesif bir sis gibi çöktüğü uzun kış gecelerinde, yatakhanenin ışıklan sönüp el ayak çekilince tarihî lise binamızın alt katlarında, loş koridorlarında vampirlerin, cadıların, cinlerin dolaştığını, geceyarıları duyduğumuz gürültülerin denizden esen rüzgârların öfkesi olmayıp, gece yaratıklarının kavga ederken çıkardıkları sesler olduğunu söylerdi. Zamanla tuhaf meraklarından kurtuldu Kenan, ama bu kez de başka takıntılar edindi kendine.
            """;
}