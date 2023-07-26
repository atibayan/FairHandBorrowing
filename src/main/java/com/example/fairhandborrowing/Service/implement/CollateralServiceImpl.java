package com.example.fairhandborrowing.Service.implement;

import com.example.fairhandborrowing.DTO.CollateralDto;
import com.example.fairhandborrowing.Model.Collateral;
import com.example.fairhandborrowing.Model.UserEntity;
import com.example.fairhandborrowing.Repository.CollateralRepository;
import com.example.fairhandborrowing.Repository.UserRepository;
import com.example.fairhandborrowing.Service.CollateralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CollateralServiceImpl implements CollateralService {

    @Autowired
    private CollateralRepository collateralRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CollateralDto> findAllCollaterals() {
        List<Collateral> collateral = collateralRepository.findAll();

        return collateral.stream().map(
            oneItem-> {
                CollateralDto dto = mapToDto(oneItem);
                dto.setInUse(false);
                return dto;
            }
        ).collect(Collectors.toList());
    }

    @Override
    public CollateralDto findOneCollateral(long id) {
        return mapToDto(collateralRepository.findById(id).get());
    }

    @Override
    public void deleteCollateral(long id) {
        collateralRepository.deleteById(id);
    }

    @Override
    public CollateralDto  createCollateral(String userName, CollateralDto collateralDto) {
        UserEntity user = userRepository.findFirstByUsername(userName);
        Collateral collateral = mapToModel(collateralDto);
        collateral.setInUse(false);
        collateral.setOwnedBy(user);

        Collateral collateralResult = collateralRepository.save(collateral);
        return mapToDto(collateralResult);
    }

    private Collateral mapToModel(CollateralDto collateralDto){
        Collateral model = Collateral.builder()
            .brand(collateralDto.getBrand())
            .itemName(collateralDto.getItemName())
            .itemCondition(collateralDto.getCondition())
            .description(collateralDto.getDescription())
            .marketValue(collateralDto.getMarketValue())
            .build();

        return model;
    }
    private CollateralDto mapToDto(Collateral collateral){
        CollateralDto collateralDto= CollateralDto.builder()
            .id(collateral.getId())
            .brand(collateral.getBrand())
            .inUse(collateral.getInUse())
            .itemName(collateral.getItemName())
            .condition(collateral.getItemCondition())
            .description(collateral.getDescription())
            .marketValue(collateral.getMarketValue())
        .build();

        return collateralDto;
    }
}
