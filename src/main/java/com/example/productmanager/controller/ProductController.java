package com.example.productmanager.controller;

import com.example.productmanager.entity.Product;
import com.example.productmanager.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", getAllCategories());
        return "product/list";
    }

    @GetMapping("/products/new")
    public String showNewForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add Product");
        return "product/form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", product.getId() == null ? "Add Product" : "Edit Product");
            return "product/form";
        }
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", product.get());
        return "product/detail";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", product.get());
        model.addAttribute("pageTitle", "Edit Product");
        return "product/form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/products/search")
    public String searchProducts(@RequestParam String keyword, Model model) {
        model.addAttribute("products", productService.searchByKeyword(keyword));
        model.addAttribute("categories", getAllCategories());
        model.addAttribute("searchKeyword", keyword);
        return "product/list";
    }

    @GetMapping("/products/category")
    public String filterByCategory(@RequestParam String category, Model model) {
        List<Product> products;
        if (category == null || category.isBlank()) {
            products = productService.getAllProducts();
        } else {
            products = productService.filterByCategory(category);
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", getAllCategories());
        model.addAttribute("selectedCategory", category);
        return "product/list";
    }

    @GetMapping("/products/sort/price-desc")
    public String sortByPriceDesc(Model model) {
        model.addAttribute("products", productService.sortByPriceDesc());
        model.addAttribute("categories", getAllCategories());
        return "product/list";
    }

    @GetMapping("/products/price-range")
    public String filterByPriceRange(@RequestParam Integer minPrice, @RequestParam Integer maxPrice, Model model) {
        model.addAttribute("products", productService.filterByPriceRange(minPrice, maxPrice));
        model.addAttribute("categories", getAllCategories());
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "product/list";
    }

    private List<String> getAllCategories() {
        return productService.getAllProducts().stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .toList();
    }
}
