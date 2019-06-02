package applications;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apis.CircularOrbitApis;
import centralobject.Person;
import circularorbits.ConcreteCircularOrbit;
import circularorbits.Label;
import exception.MyException;
import logging.LogTest;
import physicalobject.People;
import track.Track;

public class SocialNetworkCircleBigger extends ConcreteCircularOrbit<Person, People> {

  public double[][] distance;
  public List<SocialNetworkCircleTie> ties = new ArrayList<SocialNetworkCircleTie>();
  public List<People> removeObjects = new ArrayList<People>();
  public int numOfTracks;

  /**
   * add people.
   * 
   * @param name   people name
   * @param age    age
   * @param gender gender
   */

  public void addAPeople(Label name, int age, char gender) {
    LogTest.logger.info("����" + name.toString());
    // pre
    boolean flag = true;
    for (int i = 0; i < objects.size(); i++) {
      if (objects.get(i).getName().equals(name)) {
        flag = false;
      }
    }

    for (int i = 0; i < removeObjects.size(); i++) {
      if (removeObjects.get(i).getName().equals(name)) {
        flag = false;
      }
    }
    assert flag;

    for (int i = 0; i < removeObjects.size(); i++) {
      if (!objects.contains(removeObjects.get(i))) {
        objects.add(removeObjects.get(i));
      } else {
        // do nothing
      }
    }

    People p = new People(central.getName(), central.getAge(), central.getGender());
    objects.add(p);
    People p1 = new People(name, age, gender);
    objects.add(p1);
    distance = new double[objects.size() + removeObjects.size() + 1][objects.size() + removeObjects.size() + 1];
    cleanDistance();
    rel.clear();
    tracks.clear();
    relation.clear();
    buildSystem();
  }

  /**
   * getdistance.
   * 
   * @param name1 name1
   * @param name2 name2
   * @return disatance
   */

  public int getDistance(Label name1, Label name2) {
    LogTest.logger.info("����" + name1.toString() + ", " + name2.toString() + "����");
    // pre
    boolean flagA = false;
    boolean flagB = false;
    boolean flagC = false;
    for (int i = 0; i < objects.size(); i++) {
      if (objects.get(i).getName().equals(name1)) {
        flagA = true;
      }
      if (objects.get(i).getName().equals(name2)) {
        flagB = true;
      }
    }
    if (!name1.equals(name2)) {
      flagC = true;
    }

    assert flagA && flagB && flagC;

    CircularOrbitApis<Person, People> api = new CircularOrbitApis<Person, People>();
    People e1 = getCertainFriend(name1);
    People e2 = getCertainFriend(name2);
    int n = api.getLogicalDistance(this, e1, e2);
    return n;
  }

  private People getCertainFriend(Label str) {
    for (int i = 0; i < objects.size(); i++) {
      if (objects.get(i).getName().equals(str)) {
        return objects.get(i);
      }
    }
    return null;
  }

  /**
   * addTie. ����һ����ϵ���ع�
   * 
   * @param name1 ��ϵ��ʼ
   * @param name2 ��ϵ����
   * @param close ���ܶ�
   */

  public void addTie(Label name1, Label name2, double close) {
    LogTest.logger.info("����" + name1.toString() + ", " + name2.toString() + "����ϵ");
    // pre
    boolean flagA = false;
    boolean flagB = false;
    boolean flagC = false;
    for (int i = 0; i < objects.size(); i++) {
      if (objects.get(i).getName().equals(name1)) {
        flagA = true;
      }
      if (objects.get(i).getName().equals(name2)) {
        flagB = true;
      }
    }
    for (int i = 0; i < removeObjects.size(); i++) {
      if (removeObjects.get(i).getName().equals(name1)) {
        flagA = true;
      }
      if (removeObjects.get(i).getName().equals(name2)) {
        flagB = true;
      }
    }
    if (!name1.equals(name2)) {
      flagC = true;
    }
    assert flagA && flagB && flagC;

    for (int i = 0; i < removeObjects.size(); i++) {
      if (!objects.contains(removeObjects.get(i))) {
        objects.add(removeObjects.get(i));
      } else {
        // do nothing
      }
    }
    People p = new People(central.getName(), central.getAge(), central.getGender());
    objects.add(p);
    distance = new double[objects.size() + 1][objects.size() + 1];
    cleanDistance();
    rel.clear();
    tracks.clear();
    SocialNetworkCircleTie t = new SocialNetworkCircleTie(name1, name2, close);
    ties.add(t);
    relation.clear();
    buildSystem();
    // ��objects�м��м�����
    // buildSystem
  }

  /**
   * deletetie. ɾ��һ����ϵ���ع�
   * 
   * @param name1 ��ϵ��ʼ
   * @param name2 ��ϵ����
   */

  public void deleteTie(Label name1, Label name2) {

    LogTest.logger.info("ɾ��" + name1.toString() + ", " + name2.toString() + "����ϵ");
    boolean flagA = false;
    boolean flagB = false;
    for (int i = 0; i < ties.size(); i++) {
      if (ties.get(i).getName1().equals(name1) && ties.get(i).getName2().equals(name2)
          || ties.get(i).getName1().equals(name2) && ties.get(i).getName2().equals(name1)) {
        flagA = true;
      }
    }
    if (!name1.equals(name2)) {
      flagB = true;
    }
    assert flagA && flagB;

    for (int i = 0; i < removeObjects.size(); i++) {
      if (!objects.contains(removeObjects.get(i))) {
        objects.add(removeObjects.get(i));
      } else {
        // do nothing
      }
    }
    People p = new People(central.getName(), central.getAge(), central.getGender());
    objects.add(p);
    distance = new double[objects.size() + 1][objects.size() + 1];
    cleanDistance();
    Iterator<SocialNetworkCircleTie> iterator = ties.iterator();
    while (iterator.hasNext()) {
      SocialNetworkCircleTie it = iterator.next();
      if (it.getName1().equals(name1) && it.getName2().equals(name2)
          || it.getName1().equals(name2) && it.getName2().equals(name1)) {
        iterator.remove();
      }
    }
    rel.clear();
    tracks.clear();
    relation.clear();
    rel2.clear();
    buildSystem();

    // ��objects�м��м�����
    // ����ά�����Ǹ�λ������Ϊ0
    // buildSystem
  }

  private void cleanDistance() {
    for (int i = 0; i < objects.size(); i++) {
      for (int j = 0; j < objects.size(); j++) {
        distance[i][j] = 0;
      }
    }
  }

  /**
   * closelevel. ������Ϣ��ɢ��
   * 
   * @param name �ṩһ��λ�ڵ�һ������ϵ��˵�����
   * @return ��Ϣ��ɢ�� ������������ӵ������˵����ܶȵ�ƽ����
   */

  public double getCloseLevel(Label name) {
    LogTest.logger.info("����" + name.toString() + "��Ϣ��ɢ��");
    // pre
    boolean flag = false;
    Track t = null;
    for (int i = 0; i < tracks.size(); i++) {
      if (tracks.get(i).getNum() == 1) {
        t = tracks.get(i);
      }
    }
    for (int i = 0; i < rel.get(t).size(); i++) {
      if (rel.get(t).get(i).getName().equals(name)) {
        flag = true;
      }
    }
    assert flag;

    int index = 0;
    int centralIndex = 0;
    double res = 0;

    for (int i = 0; i < objects.size(); i++) {
      if (objects.get(i).getName().equals(name)) {
        index = i;
      }
      if (objects.get(i).getName().equals(central.getName())) {
        centralIndex = i;
      }
    }

    for (int i = 0; i < objects.size(); i++) {
      if (i != centralIndex) {
        if (distance[index][i] != 0) {
          res += Math.pow(distance[index][i], 2);
        }
      } else {
        // do nothing
      }
    }

    return res;
  }

  /**
   * getTrack.-* �ж�ĳ�˴����ĸ������
   * 
   * @param str �ṩĳ���˵�����
   * @return tracknum
   */

  public int getPeopleTrack(Label str) {
    Iterator<Map.Entry<Track, ArrayList<People>>> iterator = rel.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Track, ArrayList<People>> it = iterator.next();
      if (it.getValue().contains(new People(str, 0, 'F'))) {
        return it.getKey().getNum();
      }
    }
    // can't get here
    return -1;
  }

  /**
   * readfile. ��ȡ�ļ�
   */
  @Override
  public void readFile(File filename) throws IOException, MyException {
    LogTest.logger.info("��ȡ" + filename.toString() + "�ļ�");
    LogTest.logger.info("������ϵͳ");
    FileReader fr = null;
    BufferedReader br = null;
    String read = null;

    fr = new FileReader(filename);
    br = new BufferedReader(fr);
    while ((read = br.readLine()) != null) {
      if (read.contains("CentralUser")) {
        if (read.contains(".")) {
          throw new MyException("�����������䲻Ӧ��ΪС����ʽ");
        }
        centralUserSet(read);
      }

      if (read.contains("SocialTie")) {
        socialTieSet(read);
      }

      if (read.contains("Friend")) {
        if (read.contains(".")) {
          throw new MyException("����������䲻Ӧ��ΪС����ʽ");
        }
        friendSet(read);
      }
    }

    br.close();

    // ��ʼ������
    distance = new double[objects.size() + 1][objects.size() + 1];

    buildSystem();

  }

  public void buildSystem() {
    // ����
    Queue<People> que = new LinkedList<People>();
    Queue<Integer> queTrack = new LinkedList<Integer>();

    // succss
    List<People> success = new ArrayList<People>();

    for (int i = 0; i < ties.size(); i++) {
      if (ties.get(i).getName1().equals(central.getName())) {
        for (int j = 0; j < objects.size(); j++) {
          if (objects.get(j).getName().equals(ties.get(i).getName2())) {
            que.add(objects.get(j));
            queTrack.add(1);
            success.add(objects.get(j));
          }
        }
      }

      if (ties.get(i).getName2().equals(central.getName())) {
        for (int j = 0; j < objects.size(); j++) {
          if (objects.get(j).getName().equals(ties.get(i).getName1())) {
            que.add(objects.get(j));
            queTrack.add(1);
            success.add(objects.get(j));
          }
        }
      }

    }

    Track t = new Track(1);
    addTrack(t);

    while (!que.isEmpty()) {
      People p = que.poll();
      int trackIndex = queTrack.poll();
      numOfTracks = trackIndex;

      if(tracks.get(tracks.size() - 1).getNum() < trackIndex) {
        addTrack(new Track(trackIndex));
      }else {
        for(int i = 0; i < tracks.size(); i++) {
          if(tracks.get(i).getNum() == trackIndex) {
            addTrack(t);
          }
        }
      }

      for(int i = 0; i < tracks.size(); i++) {
        if(tracks.get(i).getNum() == trackIndex) {
          t = tracks.get(i);
        }
      }

      addObjectToTrack(t, p);

      for (int i = 0; i < ties.size(); i++) {

        if (ties.get(i).getName1().equals(p.getName())) {
          for (int j = 0; j < objects.size(); j++) {
            if (objects.get(j).getName().equals(ties.get(i).getName2())) {
              if(!success.contains(objects.get(j)) && !objects.get(j).getName().equals(central.getName())) {
                que.add(objects.get(j));
                queTrack.add(trackIndex + 1);
                success.add(objects.get(j));
              }
            }
          }
        }

        if (ties.get(i).getName2().equals(p.getName())) {
          for (int j = 0; j < objects.size(); j++) {
            if (objects.get(j).getName().equals(ties.get(i).getName1())) {
              if(!success.contains(objects.get(j)) && !objects.get(j).getName().equals(central.getName())) {
                que.add(objects.get(j));
                queTrack.add(trackIndex + 1);
                success.add(objects.get(j));
              }
            }
          }
        }
      }
    }

    for(int i = 0; i < objects.size(); i++) {
      boolean flag = true;
      for(int j = 0; j < success.size(); j++) {
        if(objects.get(i).getName().equals(success.get(j).getName())) {
          flag = false;
        }
      }
      if(flag) {
        removeObjects.add(objects.get(i));
      }
    }

    objects.clear();
    for(int i = 0; i < success.size(); i++) {
      objects.add(success.get(i));
    }

  }

  public int getFriendIndex(Label name) {
    for (int i = 0; i < objects.size(); i++) {
      if (objects.get(i).getName().equals(name)) {
        return i;
      }
    }
    return -1;
    // can't get here
  }

  public void centralUserSet(String str) throws MyException {

    String dealRead = null;

    String regex = "[\\<]([A-Za-z0-9\\s\\,]+)[\\>]";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(str);
    if (m.find()) {
      dealRead = m.group(1);
    }
    if (dealRead == null) {
      System.out.println("Read File Error!");
      System.exit(0);
    }
    String[] split = dealRead.split(",");

    String regex1 = "[\\s]?([A-Za-z0-9]+)[\\s]?";
    Pattern p1 = Pattern.compile(regex1);
    String[] strList = new String[3];

    for (int i = 0; i < split.length; i++) {
      Matcher m1 = p1.matcher(split[i]);
      if (m1.find()) {
        strList[i] = m1.group(1);
      }
    }

    char gender = 'F';
    Label name = null;
    int age = 0;

    name = new Label(strList[0]);
    age = Integer.parseInt(strList[1], 10);
    gender = strList[2].charAt(0);

    if (gender != 'F' && gender != 'M') {
      throw new MyException("���������Ա��������");
    }
    if (strList[2].length() != 1) {
      throw new MyException("���������Ա��������");
    }

    Person central = new Person(name, age, gender);
    setCentralObject(central);

  }

  public void socialTieSet(String str) throws MyException {

    String dealRead = null;

    String regex = "[\\<]([\\.A-Za-z0-9\\s\\,]+)[\\>]";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(str);
    if (m.find()) {
      dealRead = m.group(1);
    }
    if (dealRead == null) {
      System.out.println("Read File Error!");
      System.exit(0);
    }
    String[] split = dealRead.split(",");

    String regex1 = "[\\s]?([\\.A-Za-z0-9]+)[\\s]?";
    Pattern p1 = Pattern.compile(regex1);
    String[] strList = new String[3];

    for (int i = 0; i < split.length; i++) {
      Matcher m1 = p1.matcher(split[i]);
      if (m1.find()) {
        strList[i] = m1.group(1);
      }
    }

    Label name1 = null;
    Label name2 = null;

    name1 = new Label(strList[0]);
    name2 = new Label(strList[1]);

    if (name1.equals(name2)) {
      throw new MyException("��ϵ������ʼ��ֹ��ͬһ����");
    }

    Boolean flagn1 = false;
    Boolean flagn2 = false;
    Boolean flagname1 = false;
    Boolean flagname2 = false;
    for (int index = 0; index < objects.size(); index++) {
      if (objects.get(index).getName().equals(name1)) {
        flagn1 = true;
      }
      if (objects.get(index).getName().equals(name2)) {
        flagn2 = true;
      }
    }
    if (central.getName().equals(name1)) {
      flagname1 = true;
    }
    if (central.getName().equals(name2)) {
      flagname2 = true;
    }

    if (!(flagn1 || flagname1)) {
      throw new MyException("������ϵ����");
    }

    if (!(flagn2 || flagname2)) {
      throw new MyException("������ϵ����");
    }

    String deal = strList[2];
    int len = 0;
    len = deal.split("\\.")[1].length();

    if (len > 3) {
      throw new MyException("��������С��������");
    }

    double close = 0;
    close = Double.parseDouble(strList[2]);

    SocialNetworkCircleTie tie = new SocialNetworkCircleTie(name1, name2, close);
    ties.add(tie);
  }

  public void friendSet(String str) throws MyException {

    String dealRead = null;

    String regex = "[\\<]([A-Za-z0-9\\s\\,]+)[\\>]";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(str);
    if (m.find()) {
      dealRead = m.group(1);
    }
    if (dealRead == null) {
      System.out.println("Read File Error!");
      System.exit(0);
    }
    String[] split = dealRead.split(",");

    String regex1 = "[\\s]?([A-Za-z0-9]+)[\\s]?";
    Pattern p1 = Pattern.compile(regex1);
    String[] strList = new String[3];

    for (int i = 0; i < split.length; i++) {
      Matcher m1 = p1.matcher(split[i]);
      if (m1.find()) {
        strList[i] = m1.group(1);
      }
    }

    char gender = 'F';
    Label name = null;
    int age = 0;

    name = new Label(strList[0]);
    age = Integer.parseInt(strList[1], 10);
    gender = strList[2].charAt(0);

    if (gender != 'F' && gender != 'M') {
      throw new MyException("��������Ա��������");
    }
    if (strList[2].length() != 1) {
      throw new MyException("��������Ա��������");
    }

    People person = new People(name, age, gender);
    objects.add(person);
  }

  /**
   * GUIshow.
   * 
   * @return show String
   */

  public String guishowResult() {
    StringBuffer s = new StringBuffer();
    int counterTrack = 1;
    while (counterTrack <= numOfTracks) {
      s.append(tracks.get(counterTrack - 1).getNum() + ":\t");
      Iterator<Entry<Track, ArrayList<People>>> iterator = rel.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<Track, ArrayList<People>> it = iterator.next();
        if (it.getKey().getNum() == tracks.get(counterTrack - 1).getNum()) {
          for (int i = 0; i < it.getValue().size(); i++) {
            if (it.getValue().get(i).getName().toString().length() > 10) {
              s.append(it.getValue().get(i).getName() + "\t");
            } else {
              s.append(it.getValue().get(i).getName() + "\t\t");
            }
          }

        }
      }
      s.append("\n");
      counterTrack++;
    }
    return s.toString();
  }

  /**
   * showResult.
   * 
   */

  public void showResult() {
    int counterTrack = 1;
    while (counterTrack <= numOfTracks) {
      System.out.print(tracks.get(counterTrack - 1).getNum() + ":\t");
      Iterator<Entry<Track, ArrayList<People>>> iterator = rel.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<Track, ArrayList<People>> it = iterator.next();
        if (it.getKey().getNum() == tracks.get(counterTrack - 1).getNum()) {
          for (int i = 0; i < it.getValue().size(); i++) {
            if (it.getValue().get(i).getName().toString().length() >= 8) {
              System.out.print(it.getValue().get(i).getName() + "\t");
            } else {
              System.out.print(it.getValue().get(i).getName() + "\t\t");
            }
          }

        }
      }
      System.out.print("\n");
      counterTrack++;
    }
  }

  /**
   * GUIremoveObjects.
   * 
   * @return removed objects
   */

  public String guiremoveObjects() {
    StringBuffer s = new StringBuffer();
    for (int i = 0; i < removeObjects.size(); i++) {
      s.append(removeObjects.get(i).getName().toString() + "\t");
    }
    return s.toString();
  }

  private void setRelation() {
    for (int i = 0; i < ties.size(); i++) {
      Label name1 = ties.get(i).getName1();
      Label name2 = ties.get(i).getName2();

      if (getPeopleTrack(name1) >= 1 && getPeopleTrack(name2) >= 1) {
        People p1 = getCertainFriend(name1);
        People p2 = getCertainFriend(name2);

        if (relation.get(p1) == null) {
          List<People> arry = new ArrayList<People>();
          relation.put(p1, arry);
        }

        if (relation.get(p2) == null) {
          List<People> arry = new ArrayList<People>();
          relation.put(p2, arry);
        }

        List<People> arry1 = relation.get(p1);
        List<People> arry2 = relation.get(p2);

        arry1.add(p2);
        arry2.add(p1);

        relation.put(p1, arry1);
        relation.put(p2, arry2);
      }
    }
  }

  /**
   * showties.
   * 
   * @return ties
   */

  public String guities() {
    StringBuffer s = new StringBuffer();
    for (int i = 0; i < ties.size(); i++) {
      s.append(ties.get(i).getName1().toString() + "\t\t<-->\t" + ties.get(i).getName2().toString() + "\t\tclose level:"
          + String.valueOf(ties.get(i).getClose()));
      s.append("\n");
    }
    return s.toString();
  }

  public void outputWriter() {
    try {
      File file = new File("src/circularOrbit/test/SocialNetworkCircleOutput.txt");
      // if file doesnt exists, then create it

      if (!file.exists()) {
        file.createNewFile();
      }

      // true = append file
      FileWriter fileWritter = new FileWriter(file, true);
      FileWriter fileClean = new FileWriter(file);

      // flush
      fileClean.write("");
      fileClean.flush();
      fileClean.close();

      // write something
      fileWritter.write(
          "CentralUser ::= <" + central.getName() + "," + central.getAge() + "," + central.getGender() + ">\r\n");

      for (int i = 0; i < objects.size(); i++) {
        fileWritter.write("Friend ::= <" + objects.get(i).getName() + "," + objects.get(i).getAge() + ","
            + objects.get(i).getGender() + ">\r\n");
      }

      for (int i = 0; i < removeObjects.size(); i++) {
        fileWritter.write("Friend ::= <" + removeObjects.get(i).getName() + "," + removeObjects.get(i).getAge() + ","
            + removeObjects.get(i).getGender() + ">\r\n");
      }

      for (int i = 0; i < ties.size(); i++) {
        fileWritter.write("SocialTie ::= <" + ties.get(i).getName1() + "," + ties.get(i).getName2() + ","
            + ties.get(i).getClose() + ">\r\n");
      }

      fileWritter.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}