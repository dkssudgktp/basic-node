package Node;

import java.io.FileReader;
import java.io.IOException;

import javax.annotation.processing.SupportedSourceVersion;

public class NodeLogic implements Node{
	public static final int STsize = 100;
	public static final int HTsize = 100;
	public static final String FILE_NAME = "testdata1.txt";
	
	HTentry[] HT = new HTentry[HTsize];
	char[] ST = new char[STsize];
	int nextid = 0, nextfree = 0;
	
	String[] separators = {".",",",":",";","?","!","\t","\n"};
	
	int hashcode;
	char input;
	FileReader reader;
	
	// 파일 열어서 입력값 받아오기
	public void readInput() throws IOException {
		reader = new FileReader(FILE_NAME);
		 input = (char) reader.read();
	}
	
	// 프로그램 종료시 테이블 출력하기
	public void PrintHT() {
		  int i, HT_id;
		  HTentry node;

		  System.out.println("\n\n [[ HASH TABLE ]] \n\n");
		  for (i = 0; i < HTsize; i++) {
		    if (HT[i] != null) {
		      System.out.println("  Hash Code "+i+" : ");

		      // 테이블 안의 연결리스트 순회하면서
		      // 각 ST 값 출력하기
		      node = HT[i];
		      while (node != null) {
		        HT_id = node.index;
		        while (ST[HT_id] != '\0') {
		          System.out.println(ST[HT_id++]);
		        }
		        System.out.println("\t");
		        node = node.next;
		      }
		      System.out.println("\n");
		    }
		  }
		  System.out.println("\n");
		}
	
	// character가 숫자인지 판별
	public Boolean isDigit(char input) {
		  if (input >= '0' && input <= '9') {
		    return true;
		  }
		  return false;
		}
	
	// character가 [a-zA-Z]에 포함되는지 판별
	public Boolean isLetter(char input) {
		  if ((input >= 'a' && input <= 'z') || (input >= 'A' && input <= 'Z')) {
		    return true;
		  }
		  return false;
		}
	
	// character가 seperator인지 판별
	public Boolean isSeparator(char string) {
		  int len, i;
		  len = separators.length;
		  for (i = 0; i < len; i++) {
		    if (separators[i] == Character.toString(string)) {
		    	System.out.println("1");
		      return true;
		    }
		  }
		  return false;
		}
	
	// 해시 코드 계산
	public void hash(int nextid, int nextfree) {
		  int i;
		  int code = 0;

		  for (i = nextid; i < nextfree; i++) {
		    code += (int) ST[i];
		    hashcode = code % HTsize;
		  }
		}
	
	
	// 테이블에 현재 들어온 입력값이 존재하는지 확인
	// 미존재 시 -1 반환, 존재 시 index값 반환
	public int lookupHT(int nextid, int nextfree) {
		  HTentry node;
		  int HT_id;
		  int HT_check = -1;
		  int i;
		  boolean exist;

		  // 동일한 해시코드값을 가진 테이블의 연결리스트 순회
		  node = HT[hashcode];
		  while (node != null) {
		    HT_id = node.index;
		    exist = true;

		    // 연결리스트 노드의 해당하는 스트링값과 비교
		    for (i = nextid; i < nextfree; i++) {
		      if (ST[i] != ST[HT_id]) {
		        exist = false;
		        break;
		      }
		      HT_id++;
		    }

		    if (ST[HT_id] != '\0') exist = false;

		    // 동일한 입력값이 존재함.
		    if (exist) return node.index;

		    node = node.next;
		  }
		  return HT_check;
		}
	
	// 테이블 안 연결리스트에 구조체 생성 및
	// 연결 리스트 앞쪽에다가 노드 삽입
	public void insertHT() {
		HTentry ptr;

		ptr = new HTentry();
		ptr.index = nextid;
		ptr.next = HT[hashcode];
		HT[hashcode] = ptr;
	}
	
	public void mainLogic() throws IOException{
		int temp, i;
		boolean invalid = false;
		  System.out.println("\n");
		  System.out.println(" -----------\t-----------\n");
		  System.out.println(" Index in ST\tIdentifier\n");
		  System.out.println(" -----------\t-----------\n");
		  System.out.println("\n");

		  readInput();
		  
		  // 입력 파일 끝까지 아니면 오버플로우 일어날때까지 while loop 실행
		  while ((nextid == nextfree)||(input != -1)) {
			  System.out.println(nextid);
		    // ST 오버플로우 발생
		    if (nextfree >= STsize) {
		      System.out.println(" ...Error... \t");
		      System.out.println("\t\t(Overflow)");
		      nextfree = nextid - 1;
		      System.out.println(nextid);
		      break;
		    }

		    if (!isSeparator(input) && input != -1) {
		      // ST에 입력값 복사
		      ST[nextfree++] = input;
		      // 허용되지 않은 character 값 판별
		      if (!isDigit(input) && input != '_' && !isLetter(input)) invalid = true;
		      input = (char) reader.read();
		    } else {
		      // 다른 seperator들 전부 skip
		      while (isSeparator(input)) {
		        input = (char) reader.read();
		        if (Character.isWhitespace(input)) break;
		      }
		      // 허용되지 않은 character 여부 판별
		      if (invalid) {
		        System.out.println(" ...Error...\t");
		        for (i = nextid; i < nextfree; i++) {
		          System.out.println(ST[i]);
		        }
		        System.out.println("\t\t(Invalid character)\n");

		        // nextfree 위치를 전의 nextid 위치로 돌려 놓음
		        nextfree = nextid;
		        invalid = false;
		      }
		      // 숫자로 시작하는지 여부 판별
		      else if (isDigit(ST[nextid])) {
		        System.out.println(" ...Error...\t");
		        for (i = nextid; i < nextfree; i++) {
		        	System.out.println(ST[i]);
		        }
		        System.out.println("\t\t(start with digit)\n");

		        // nextfree 위치를 전의 nextid 위치로 돌려 놓음
		        nextfree = nextid;
		      }
		      // 입력 단어가 10자 초과 여부 판별
		      else if (nextfree - nextid > 10) {
		        System.out.println(" ...Error...\t");
		        for (i = nextid; i < nextfree; i++) {
		        	System.out.println(ST[i]);
		        }
		        System.out.println("\t(too long identifier)\n");

		        // nextfree 위치를 전의 nextid 위치로 돌려 놓음
		        nextfree = nextid;
		      } else {
		        // 해시코드값 계산
		        hash(nextid, nextfree);
		        // 이미 테이블 안에 같은 해시값이 존재하는지 확인
		        temp = lookupHT(nextid, nextfree);
		        if (temp != -1) {
		          // 동일한 입력값 존재
		          System.out.println(temp);
		          for (i = nextid; i < nextfree; i++) {
		        	  System.out.println(ST[i]);
		          }
		          System.out.println("\t\t(already existed)\n");

		          nextfree = nextid;
		        } else {
		          // 테이블에 삽입
		          insertHT();
		          System.out.println(nextid);
		          for (i = nextid; i < nextfree; i++) {
		        	  System.out.println(ST[i]);
		          }
		          if (nextfree - nextid > 7) {
		            System.out.println("\t(entered)\n");
		          } else {
		            System.out.println("\t\t(entered)\n");
		          }

		          // ST에 null character 삽입
		          ST[nextfree++] = '\0';
		          // nextid의 위치를 nextfree로 이동
		          nextid = nextfree;
		        }
		      }
		    }
		  }

		  // 테이블 출력
		  PrintHT();

		  System.out.println(" <"+nextfree+"characters are used in the string table>\n");
		for (int j = 0; j < ST.length; j++) {
			System.out.println(ST[j]);
		}
	}
}
