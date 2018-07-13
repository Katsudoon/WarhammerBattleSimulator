package a2018.warhammer.alex.warhammerbattlesimulator.models;

public class UtilisateurModel {

    private String mail;
    private String password;
    private int token;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public UtilisateurModel(String mail, String password, int token) {
        this.mail = mail;
        this.password = password;
        this.token = token;
    }
}
