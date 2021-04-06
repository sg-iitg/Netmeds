package myTestPackage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main{
	public static  void main(String[] args) throws IOException, InterruptedException{

		WebDriver driver;
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.netmeds.com/prescriptions");
		Thread.sleep(5000);
		
		FileWriter fos = new FileWriter("Meds.tsv");
		PrintWriter dos = new PrintWriter(fos);
		dos.println("Medicine Name\tDrug Used\tDrug Category\t");
		
		char char_iterator= 'a';

		while(char_iterator <= 'a') //we can iterate upto z to scrape all drug categories
		{
			Document doc = Jsoup.parse(driver.getPageSource());
			Elements page = ((Element) doc).getElementsByClass("drug-list-col ln-"+ char_iterator);
			
			Document htmldoc = Jsoup.parse(page.html());
			
			Elements list = htmldoc.body().getElementsByTag("li");
			
			int t=1;
			
			
			for (Element x: list) {
				String url = x.getElementsByTag("a").attr("href");
				driver.get(url);
				Document dc = Jsoup.parse(driver.getPageSource());
				
				char inner_chariterator= 'a';

				while(inner_chariterator <= 'h') //we can iterate upto z to scrape all drugs
				{
					Elements med_page = ((Element) dc).getElementsByClass("drug-list-col ln-"+ inner_chariterator);
					
					Document meddoc = Jsoup.parse(med_page.html());
					
					Elements lst = meddoc.body().getElementsByTag("li");
					
					for(Element r: lst) {
						String uri = r.getElementsByTag("a").attr("href");
						driver.get(uri);
						
						Document d = Jsoup.parse(driver.getPageSource());
						
						Elements drug_name = ((Element) d).getElementsByClass("product-detail");
						Document html_obtained = Jsoup.parse(drug_name.html());
	
						dos.append(html_obtained.getElementsByClass("black-txt").text() + "\t");
						dos.append(html_obtained.getElementsByClass("drug-manu").text() +"\t");
						dos.append(html_obtained.getElementsByClass("gen_drug ellipsis").text() +"\t");
						dos.println();
					}
					inner_chariterator++;
				}
				t++;		//this has been used just to end the script early 
				if(t==4)	
				{
					break;
				}
			}
			char_iterator++;
		}
		dos.close();
		fos.close();	
	}
}
