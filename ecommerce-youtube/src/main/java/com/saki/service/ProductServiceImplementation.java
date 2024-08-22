package com.saki.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.saki.exception.ProductException;
import com.saki.model.Category;
import com.saki.model.Product;
import com.saki.model.Size;
import com.saki.repository.ProductRepository;
import com.saki.repository.CategoryRepository;
import com.saki.request.CreateProductRequest;

@Service
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository,
                                        CategoryRepository categoryRepository, UserService userService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    public Product createProduct(CreateProductRequest req) {
        Category topLevel = getOrCreateCategory(req.getTopLavelCategory(), 1, null);
        Category secondLevel = getOrCreateCategory(req.getSecondLavelCategory(), 2, topLevel.getName());
        Category thirdLevel = getOrCreateCategory(req.getThirdlavelCategory(), 3, secondLevel.getName());

        Product product = new Product();
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(parseIntSafely(req.getDiscountPrice()));
        product.setDiscountPercent(parseIntSafely(req.getDiscountPresent()));
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(parseIntSafely(req.getPrice()));
        product.setSizes(req.getSize().stream().map(Size::toString).collect(Collectors.toSet()));
        product.setQuantity(parseIntSafely(req.getQuantity()));
        product.setCategories(Set.of(thirdLevel));
        product.setCreatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    private Category getOrCreateCategory(String categoryName, int level, String parentCategoryName) {
        Category category = categoryRepository.findByNameAndParent(categoryName, parentCategoryName);
        if (category == null) {
            Category parentCategory = (parentCategoryName != null) ? 
                categoryRepository.findByName(parentCategoryName) : null;

            category = new Category();
            category.setName(categoryName);
            category.setLevel(level);
            category.setParentCategory(parentCategory);
            category = categoryRepository.save(category);
        }
        return category;
    }

    @Override
    public String updateProduct(Long productId, CreateProductRequest req) throws ProductException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));

        Category category = categoryRepository.findByName(req.getThirdlavelCategory());
        if (category == null) {
            throw new ProductException("Category not found");
        }

        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setDescription(req.getDescription());
        product.setDiscountedPrice(parseIntSafely(req.getDiscountPrice()));
        product.setDiscountPercent(parseIntSafely(req.getDiscountPresent()));
        product.setImageUrl(req.getImageUrl());
        product.setBrand(req.getBrand());
        product.setPrice(parseIntSafely(req.getPrice()));
        product.setSizes(req.getSize().stream().map(Size::toString).collect(Collectors.toSet()));
        product.setQuantity(parseIntSafely(req.getQuantity()));
        product.setCategories(Set.of(category));

        productRepository.save(product);

        return "Product updated successfully";
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice,
                                       Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        int pageNum = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10; // Default pageSize if not provided

        Sort sorting = "price_high".equalsIgnoreCase(sort) ? Sort.by(Sort.Order.desc("discountedPrice")) : Sort.by(Sort.Order.asc("discountedPrice"));

        Pageable pageable = PageRequest.of(pageNum, size, sorting);

        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, null, sort);
        System.out.println("Products before filtering: " + products.size());

        products = filterProducts(products, colors, sizes, stock);
        System.out.println("Products after filtering: " + products.size());

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageable, products.size());
    }

   
    private List<Product> filterProducts(List<Product> products, List<String> colors, List<String> sizes, String stock) {
        if (colors != null && !colors.isEmpty()) {
            products = products.stream()
                               .filter(p -> p.getColor() != null &&
                                            colors.stream()
                                                  .anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                               .collect(Collectors.toList());
        }

        if (sizes != null && !sizes.isEmpty()) {
            products = products.stream()
                               .filter(p -> p.getSizes() != null &&
                                            p.getSizes().stream()
                                              .anyMatch(size -> sizes.contains(size)))
                               .collect(Collectors.toList());
        }

        if (stock != null) {
            if ("in_stock".equalsIgnoreCase(stock)) {
                products = products.stream()
                                   .filter(p -> p.getQuantity() > 0)
                                   .collect(Collectors.toList());
            } else if ("out_of_stock".equalsIgnoreCase(stock)) {
                products = products.stream()
                                   .filter(p -> p.getQuantity() < 1)
                                   .collect(Collectors.toList());
            }
        }

        return products;
    }

    
    
    
    
    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + productId));
        productRepository.delete(product);
        return "Product deleted successfully";
    }

    private int parseIntSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0; // Handle appropriately
        }
    }
}
