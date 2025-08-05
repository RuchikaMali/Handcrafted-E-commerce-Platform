//UserProductController = ProductController
package com.zosh.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.exception.ProductException;
import com.zosh.modal.Product;
import com.zosh.service.ProductService;
import com.zosh.user.domain.ProductSubCategory;

@RestController
@RequestMapping("/api")
public class UserProductController {
	
	@Autowired
	private ProductService productService;
	
	public UserProductController(ProductService productService) {
		this.productService=productService;
	}


	@GetMapping("/products")
	public ResponseEntity<Page<Product>> findProductByCategoryHandler(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) List<String> color,
			@RequestParam(required = false) List<String> size,
			@RequestParam(required = false) List<String> material,
			@RequestParam(required = false) Integer minPrice,
			@RequestParam(required = false) Integer maxPrice,
			@RequestParam(required = false) Integer minDiscount,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String stock,
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "10") Integer pageSize
	) {

		if (category != null) {
			category = toCamelCase(category.replace('_', ' '));
		}

		Page<Product> res = productService.getAllProduct(
				category != null ? category : "",
				color != null ? color : List.of(),
				size != null ? size : List.of(),
				material != null ? material : List.of(),
				minPrice != null ? minPrice : 0,
				maxPrice != null ? maxPrice : Integer.MAX_VALUE,
				minDiscount != null ? minDiscount : 0,
				sort != null ? sort : "asc",
				stock != null ? stock : "",
				pageNumber,
				pageSize
		);

		return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
	}
	
	public static String toCamelCase(final String init) {
	    if (init == null)
	        return null;

	    final StringBuilder ret = new StringBuilder(init.length());

	    for (final String word : init.split(" ")) {
	        if (!word.isEmpty()) {
	            ret.append(Character.toUpperCase(word.charAt(0)));
	            ret.append(word.substring(1).toLowerCase());
	        }
	        if (!(ret.length() == init.length()))
	            ret.append(" ");
	    }

	    return ret.toString();
	}

	
	@GetMapping("/products/id/{productId}")
	public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException{
		
		Product product=productService.findProductById(productId);
		
		return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/category/productId/{productId}")
	public ResponseEntity<List<Product>> findProductByCategory(@PathVariable Long productId) throws ProductException{
		
		Product product=productService.findProductById(productId);
		List<Product> products=productService.findProductByCategoryId(product.getCategory().getId());
		
		return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
	}
	
	
	
	

	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q){
		
		List<Product> products=productService.searchProduct(q);
		
		return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
		
	}
}