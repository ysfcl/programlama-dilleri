package proje;

class Token {
    String icerik, tip;

    Token(String icerik, String tip) {
        this.icerik = icerik;
        this.tip = tip;
    }

    @Override
    public String toString() {
        return icerik + "->" + tip;
    }
}
