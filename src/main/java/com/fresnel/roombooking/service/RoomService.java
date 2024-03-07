package com.fresnel.roombooking.service;


import com.fresnel.roombooking.exception.InternalServerException;
import com.fresnel.roombooking.exception.ResourceNotFoundException;
import com.fresnel.roombooking.model.Room;
import com.fresnel.roombooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author fresnel
 */

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;

    private final DataSource dataSource;
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
/*
    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(theRoom.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Room not found!");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }*/
@Override
public byte[] getRoomPhotoByRoomId(Long roomId) {
    try (Connection connection = dataSource.getConnection()) {
        connection.setAutoCommit(false); // Désactiver le mode auto-commit

        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, Room not found!");
        }

        Blob photoBlob = theRoom.get().getPhoto();
        if (photoBlob != null) {
            byte[] photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            connection.commit(); // Commit de la transaction
            return photoBytes;
        }

        return null;
    } catch (SQLException e) {
        // Gérer l'exception SQL
        e.printStackTrace(); // À remplacer par une gestion d'exception appropriée
        return null;
    }
}


    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(theRoom.isPresent()){
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Room room = roomRepository.findById(roomId).get();
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex) {
                throw new InternalServerException("Fail updating room");
            }
        }
       return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }
}
