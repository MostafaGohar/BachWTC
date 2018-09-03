package gohar.mostafa.bachwtc;

public class Song {
    int bookNumber;
    int songNumber;
    String name;


    public Song(int bookNumber, int songNumber, String name) {
        this.bookNumber = bookNumber;
        this.songNumber = songNumber;
        this.name = name;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
