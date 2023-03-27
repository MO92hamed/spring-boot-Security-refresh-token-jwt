package com.itgate.ecommerce.controllers;


import com.itgate.ecommerce.models.Product;
import com.itgate.ecommerce.models.Provider;
import com.itgate.ecommerce.models.Subcategory;
import com.itgate.ecommerce.service.impl.ProductServiceImpl;
import com.itgate.ecommerce.service.impl.ProviderServiceImpl;
import com.itgate.ecommerce.service.impl.SubcategoryServiceImpl;
import com.itgate.ecommerce.utils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    ProductServiceImpl productService;
    @Autowired
    ProviderServiceImpl providerServiceImpl;

    @Autowired
    SubcategoryServiceImpl subcategoryServiceImpl;
    @Autowired
    private StorageService storageService;
    private final Path rootLocation = Paths.get("upload-dir");

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id){
        return productService.findById(id);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.findAllProducts();
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteProduct(@PathVariable Long id){
        Map<String, Boolean> response = new HashMap<>();
        try{
            productService.deleteByID(id);

            response.put("Deleted", Boolean.TRUE);
            return response;
        } catch(Exception e){
            response.put("Failed", Boolean.FALSE);
            return response;
        }

    }

    @PostMapping("provider/{idprovider}/subcategory/{idsubcategory}")
    public Product addProduct(Product product,
                              @PathVariable Long idprovider,
                              @PathVariable Long idsubcategory,
                              @RequestParam("file") MultipartFile file){
       /* storageService.store(file);
        product.setImage(file.getOriginalFilename());*/
        try{
            String fileName = Integer.toString(new Random().nextInt(1000000000));
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'), file.getOriginalFilename().length());
            String name = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + fileName + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            product.setImage(original);

            Provider provider = providerServiceImpl.findProvierById(idprovider);
            Subcategory subcategory = subcategoryServiceImpl.findSubById(idsubcategory);
            product.setProvider(provider);
            product.setSubcategory(subcategory);

        }catch(Exception e){
            throw new RuntimeException("Fail file Problem Backend !!");
        }
        return productService.addProduct(product);
    }
    @PostMapping
    public Product createproduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product){
        Product p1 = productService.findById(id);
        if (p1 != null){
            product.setSubcategory(p1.getSubcategory());
            product.setProvider(p1.getProvider());
            return productService.updateProduct(product);
        }
        else
            throw new RuntimeException("FAIL!!");
    }

    //affiche Image
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = storageService.LoadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + file.getFilename() + "\"")
                .body(file);

    }

}
