public class DynamoTerm {
  public String uuid;
  public String scheduled_for;
  public Integer scheduling_interval;
  public String term_a;
  public String term_b;
  public DynamoTermMeta meta;

  public void print() {
    System.out.printf("UUID: %s\t%s\t%s\n%s\n", uuid, scheduled_for, scheduling_interval, term_a);
  }
}