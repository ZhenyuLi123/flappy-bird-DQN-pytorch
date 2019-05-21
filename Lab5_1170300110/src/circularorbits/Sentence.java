package circularorbits;

public class Sentence {
  private String str;

  public Sentence(String str) {
    this.str = str;
    checkRep();
  }

  private void checkRep() {
    boolean flag = false;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) >= 'a' && str.charAt(i) <= 'z' 
          || str.charAt(i) >= 'A' && str.charAt(i) <= 'Z'
          || str.charAt(i) >= '0' && str.charAt(i) <= '9' || str.charAt(i) == ' ') {
        // do nothing
      } else {
        flag = true;
      }
    }
    System.out.println(flag);
    assert (flag == false);
  }

  public String getSentence() {
    return this.str;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (((Sentence) o).getSentence().equals(this.str)) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    // TODO Auto-generated method stub
    return super.hashCode();
  }

  @Override
  public String toString() {
    return str;
  }

}