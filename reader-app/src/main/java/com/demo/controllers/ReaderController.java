package com.demo.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.clients.BookClient;
import com.demo.entities.Book;
import com.demo.entities.Reader;
import com.demo.repositories.ReaderRepository;
import com.demo.services.ReaderService;


@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://hoppscotch.io/"})
@RequestMapping("/reader/api/v1")
public class ReaderController {
	
	@Autowired
	private ReaderRepository repo;
	
	@Autowired
	private BookClient bookClient;
	
	@Autowired
	private ReaderService readerService;
	
//	@Autowired
//	private Reader r;
	
	//public Reader r = new Reader();
	public static int pay = 10000;
	
	@GetMapping("")
 	public String test(){
		
		//return s;
		return "testing";
	}

	@GetMapping("/getBookByCategory")
 	public List<Book> getBookByCategory(@RequestParam String category){
		
		//return s;
		return bookClient.getBookByCategory(category);
	}
	@GetMapping("/getBookByAuthor")
 	public List<Book> getBookByAuthor(@RequestParam String author){
		
		//return s;
		return bookClient.getBookByAuthor(author);
	}
	
	@GetMapping("/getBookByPublisher")
 	public List<Book> getBookByPublisher(@RequestParam String publisher){
		
		//return s;
		return bookClient.getBookByPublisher(publisher);
	}
	
	@GetMapping("/getBookByPrice")
 	public List<Book> getBookByPrice(@RequestParam int price){
		
		//return s;
		return bookClient.getBookByPrice(price);
	}
	@GetMapping("/getPurchasedBookByEmail")
 	public List<Reader> getPurchasedBookByEmail(@RequestParam String readerEmail){
		
		//return s;
		return readerService.getPurchasedBookByEmail(readerEmail);
	}
	
	@GetMapping("/getPurchasedBookByBookIdAndEmail")
 	public List<Reader> getPurchasedBookByBookIdAndEmail(@RequestParam int id , @RequestParam String readerEmail){
		
		//return s;
		return readerService.getPurchasedBookByBookIdAndEmail(id , readerEmail);
	}
	
	@GetMapping("/getPurchasedBookByBookPayIdAndEmail")
 	public Reader getPurchasedBookByPayIdAndEmail(@RequestParam int payId , @RequestParam String readerEmail){
		
		//return s;
		return readerService.getPurchasedBookByPayIdAndEmail(payId , readerEmail);
	}
	
	
	@PostMapping("/buy")
 	public Reader buyBook(@RequestParam int id, @RequestParam String readerName, @RequestParam String readerEmail){
		
		
		Reader r = new Reader();
		
		r.setReaderName(readerName);
		r.setReaderEmail(readerEmail);
	Book book= bookClient.buyBook(id);
		System.out.println(book);
		r.setId(book.getId());
		r.setTitle(book.getTitle());
	r.setAuthor(book.getAuthor());
	r.setBlock(book.getBlock());
	r.setCategory(book.getCategory());
	r.setContent(book.getContent());
	r.setPublisher(book.getPublisher());
	r.setPrice(book.getPrice());
	r.setPayTime(LocalDateTime.now());
	//System.out.println(r);
	//r.setPayId(pay++);
	
	System.out.println(r);
		return  readerService.saveBook(r);
	}
	
	@DeleteMapping("/deleteBookByPayIdAndEmail")
 	public String deleteBookByPayIdAndEmail(@RequestParam int payId , @RequestParam String readerEmail){
		
		//return s;
		 Reader r = readerService.getPurchasedBookByPayIdAndEmail(payId , readerEmail);
		 
		 System.out.println(r);
		 
	 if(r!= null) {
		 long hours = ChronoUnit.HOURS.between( r.getPayTime() , LocalDateTime.now());
		 System.out.println(hours);
		 if(hours < 24)
		 {
			  readerService.deleteBookByPayIdAndEmail(payId);
			  return " book deleted" ;
		 }
	 else
		 return " Book Doen't exists in your list " ;
		 }
	 
	 else {
		 return " Book Doen't exists in your list " ;
	}
	}
	
	
}
