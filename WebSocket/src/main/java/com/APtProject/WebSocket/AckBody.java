package com.APtProject.WebSocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AckBody {

    private Integer index;
    private String character;
    private Boolean isDelete;
    private Boolean isAck;
    private Boolean isBold;
    private Boolean isItalic;
    private Boolean isInsert;
    private Integer version;
    private Boolean isVersion;

    @Override
    protected AckBody clone() {
        AckBody ackBody = new AckBody();
        ackBody.setIndex(this.index);
        ackBody.setCharacter(this.character);
        ackBody.setIsDelete(this.isDelete);
        ackBody.setIsBold(this.isBold);
        ackBody.setIsItalic(this.isItalic);
        ackBody.setIsInsert(this.isInsert);
        ackBody.setIsAck(this.isAck);
        ackBody.setVersion(this.version);
        return ackBody;
    }
}
