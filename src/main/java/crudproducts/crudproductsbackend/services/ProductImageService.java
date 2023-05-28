package hr.hrproduct.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import hr.hrproduct.dto.ProductImage.ProductImageSimplifiedDto;
import hr.hrproduct.entities.ProductImage;
import hr.hrproduct.repositories.ProductImageRepository;
import hr.hrproduct.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;

    private final ProductRepository productRepository;

    private final UserService userService;

    private static final String CLOUDINARY_CLOUD_NAME = "dgnktskkl";
    private static final String CLOUDINARY_API_KEY = "464873712429871";
    private static final String CLOUDINARY_API_SECRET = "IKPV7SEQdUGwhpbSBdjVX1f145g";

    public List<ProductImage> uploadImage(final List<MultipartFile> files,
                                          final Long productId,
                                          final HttpServletRequest request) {
        // rulesOfUpload(files, filePrincipal);
        final List<ProductImage> listProductImage = setProductImage(files, productId, request);
        return productImageRepository.saveAll(listProductImage);
    }

    public List<ProductImageSimplifiedDto> findAllByProductId(final Long productId, final HttpServletRequest request) {
        final Long userId = userService.getIdUserByToken(request);
        final List<ProductImage> listProductImage = productImageRepository.findAllByProductIdAndUserId(productId, userId);
        return getProductImageSimplifiedDtos(listProductImage);
    }

    private static List<ProductImageSimplifiedDto> getProductImageSimplifiedDtos(List<ProductImage> listProductImage) {
        final List<ProductImageSimplifiedDto> listProductImageSimplifiedDto = new ArrayList<>();

        listProductImage.forEach(productImage -> {
            ProductImageSimplifiedDto productImageSimplifiedDto = new ProductImageSimplifiedDto();
            productImageSimplifiedDto.setId(productImage.getId());
            productImageSimplifiedDto.setName(productImage.getName());
            productImageSimplifiedDto.setType(productImage.getType());
            productImageSimplifiedDto.setPrincipal(productImage.getPrincipal());
            productImageSimplifiedDto.setUrl(productImage.getUrl());
            listProductImageSimplifiedDto.add(productImageSimplifiedDto);
        });
        return listProductImageSimplifiedDto;
    }

    @Transactional
    public void delete(final Long id, final HttpServletRequest request) {
        productImageRepository.deleteByIdAndUserId(id, userService.getIdUserByToken(request));
    }

    // rules of upload
    public void rulesOfUpload(final List<MultipartFile> files, final String filePrincipal) {
        maxFourImages(files);
        //hasJustOnePrincipal(files, filePrincipal);
    }

    public void hasJustOnePrincipal(final List<MultipartFile> files, final String filePrincipal) {
        boolean hasPrincipal = false;

        for (MultipartFile file : files) {
            if (file.getOriginalFilename().equals(filePrincipal)) {
                if (hasPrincipal) {
                    throw new IllegalArgumentException("Only one principal image can be sent");
                }
                hasPrincipal = true;
            }
        }

        if (!hasPrincipal) {
            throw new IllegalArgumentException("You must send a principal image");
        }
    }


    public void maxFourImages(final List<MultipartFile> files) {
        if (files.size() > 4) {
            throw new RuntimeException("Just 4 images can be send it");
        }
    }

    private List<ProductImage> setProductImage(final List<MultipartFile> files,
                                               final Long productId,
                                               final HttpServletRequest request) {

        final List<ProductImage> listProductImage = new ArrayList<>();

        files.stream().forEach(file -> {
            final ProductImage productImage = new ProductImage();
            productImage.setName(file.getOriginalFilename());
            productImage.setType(file.getContentType());
            productImage.setUser(userService.getUserByToken(request));
            try {
                productImage.setUrl(getUrlFile(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            setProduct(productId, productImage);

            listProductImage.add(productImage);
        });
        return listProductImage;
    }

    private void setProduct(final Long productId, final ProductImage productImage) {
        productRepository.findById(productId).map(product -> {
            productImage.setProduct(product);
            return product;
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

//    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
//        File file = new File(multipartFile.getOriginalFilename());
//        FileCopyUtils.copy(multipartFile.getBytes(), file);
//        return file;
//    }

    public static String getUrlFile(final MultipartFile multipartFile) throws IOException {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUDINARY_CLOUD_NAME,
                "api_key", CLOUDINARY_API_KEY,
                "api_secret", CLOUDINARY_API_SECRET));


//        final File imageFile = new File(multipartFile.getOriginalFilename());
//        FileCopyUtils.copy(multipartFile.getBytes(), imageFile);

        // muda o diretorio para o diretorio do projeto

        String directoryPath = "/src/main/resources/static/images/";
        String fileName = multipartFile.getOriginalFilename();
        File directory = new File(directoryPath);

        // Cria o diretório, se não existir
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Cria o arquivo com o caminho completo do diretório e nome do arquivo
        File file = new File(directory, fileName);

        // Copia o conteúdo do MultipartFile para o novo arquivo
        FileCopyUtils.copy(multipartFile.getBytes(), file);


        final Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

        return (String) uploadResult.get("secure_url");
    }

    @Transactional
    public void principalImage(final Long productId, final Long productImageId, final HttpServletRequest request) {
        productImageRepository.setPrincipal(productId, productImageId, userService.getIdUserByToken(request));
    }
}
