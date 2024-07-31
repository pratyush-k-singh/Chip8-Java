public class Instructions {

    public void cls(Chip8 chip8) {
        for (int i = 0; i < chip8.SCREEN_HEIGHT; i++) {
            for (int j = 0; j < chip8.SCREEN_WIDTH; j++) {
                chip8.screen[i][j] = 0;
            }
        }
        chip8.drawScreenFlag = chip8.TRUE;
        chip8.pcReg += 2;
    }

    public void returnFromSubroutine(Chip8 chip8) {
        chip8.spReg--;
        chip8.pcReg = chip8.stack[spReg];
        chip8.pcReg += 2;
    }

    public void jump(Chip8 chip8) {
        int nnn = chip8.currentOp & 0x0FFF;
        chip8.pcReg = nnn;
    }

    public void callSubroutine(Chip8 chip8) {
        int nnn = chip8.currentOp & 0x0FFF;
        chip8.stack[chip8.spReg] = chip8.pcReg;
        chip8.spReg++;
        chip8.pcReg = nnn;
    }

    public void seVxKk(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;
        int kk = chip8.currentOp & 0x00FF;

        if (chip8.V[targetVReg] == kk) {
            chip8.pcReg += 4;
        } else {
            chip8.pcReg += 2;
        }
    }

    public void sneVxKk(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;
        int kk = chip8.currentOp & 0x00FF;

        if (chip8.V[targetVReg] != kk) {
            chip8.pcReg += 4;
        } else {
            chip8.pcReg += 2;
        }
    }

    public void seVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        if (chip8.V[targetVRegX] == chip8.V[targetVRegY]) {
            chip8.pcReg += 4;
        } else {
            chip8.pcReg += 2;
        }
    }

    public void ldVx(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;
        int kk = chip8.currentOp & 0x00FF;

        chip8.V[targetVReg] = kk;
        chip8.pcReg += 2;
    }

    public void addVxImm(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;
        int kk = chip8.currentOp & 0x00FF;

        chip8.V[targetVReg] += kk;
        chip8.pcReg += 2;
    }

    public void moveVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        chip8.V[targetVRegX] = chip8.V[targetVRegY];
        chip8.pcReg += 2;
    }

    public void orVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        chip8.V[targetVRegX] |= chip8.V[targetVRegY];
        chip8.pcReg += 2;
    }

    public void andVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        chip8.V[targetVRegX] &= chip8.V[targetVRegY];
        chip8.pcReg += 2;
    }

    public void xorVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        chip8.V[targetVRegX] ^= chip8.V[targetVRegY];
        chip8.pcReg += 2;
    }

    public void addVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;
        int sum = chip8.V[targetVRegX] + chip8.V[targetVRegY];

        if (sum > 255) {
            chip8.V[0xF] = 1;
        } else {
            chip8.V[0xF] = 0;
        }

        chip8.V[targetVRegX] = sum & 0xFF;
        chip8.pcReg += 2;
    }

    public void subVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        if (chip8.V[targetVRegX] > chip8.V[targetVRegY]) {
            chip8.V[0xF] = 1;
        } else {
            chip8.V[0xF] = 0;
        }

        chip8.V[targetVRegX] -= chip8.V[targetVRegY];
        chip8.pcReg += 2;
    }

    public void shr(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;

        if (chip8.V[targetVRegX] % 2 == 1) {
            chip8.V[0xF] = 1;
        } else {
            chip8.V[0xF] = 0;
        }

        chip8.V[targetVRegX] >>= 1;
        chip8.pcReg += 2;
    }

    public void subnVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        if (chip8.V[targetVRegY] > chip8.V[targetVRegX]) {
            chip8.V[0xF] = 1;
        } else {
            chip8.V[0xF] = 0;
        }

        chip8.V[targetVRegX] = chip8.V[targetVRegY] - chip8.V[targetVRegX];
        chip8.pcReg += 2;
    }

    public void shl(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;

        if ((chip8.V[targetVRegX] & 0x80) != 0) {
            chip8.V[0xF] = 1;
        } else {
            chip8.V[0xF] = 0;
        }

        chip8.V[targetVRegX] <<= 1;
        chip8.pcReg += 2;
    }

    public void sneVxVy(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;

        if (chip8.V[targetVRegX] != chip8.V[targetVRegY]) {
            chip8.pcReg += 4;
        } else {
            chip8.pcReg += 2;
        }
    }

    public void ldi(Chip8 chip8) {
        int nnn = chip8.currentOp & 0x0FFF;
        IReg = nnn;
        chip8.pcReg += 2;
    }

    public void jumpV0(Chip8 chip8) {
        int nnn = chip8.currentOp & 0x0FFF;
        chip8.pcReg = nnn + chip8.V[0];
    }

    public void rnd(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;
        int kk = chip8.currentOp & 0x00FF;
        int randomNum = (int) (Math.random() * 256);

        chip8.V[targetVReg] = randomNum & kk;
        chip8.pcReg += 2;
    }

    public void drw(Chip8 chip8) {
        int targetVRegX = (chip8.currentOp & 0x0F00) >> 8;
        int targetVRegY = (chip8.currentOp & 0x00F0) >> 4;
        int spriteHeight = chip8.currentOp & 0x000F;
        int xLocation = chip8.V[targetVRegX];
        int yLocation = chip8.V[targetVRegY];
        int pixel;

        chip8.V[0xF] = 0;
        for (int yLine = 0; yLine < spriteHeight; yLine++) {
            pixel = chip8.ram[IReg + yLine];
            for (int xLine = 0; xLine < 8; xLine++) {
                if ((pixel & (0x80 >> xLine)) != 0) {
                    if (chip8.screen[yLocation + yLine][xLocation + xLine] == 1) {
                        chip8.V[0xF] = 1;
                    }
                    chip8.screen[yLocation + yLine][xLocation + xLine] ^= 1;
                }
            }
        }

        chip8.drawScreenFlag = TRUE;
        chip8.pcReg += 2;
    }

    public void skp(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;

        if (keyboard[chip8.V[targetVReg]]) {
            chip8.pcReg += 4;
        } else {
            chip8.pcReg += 2;
        }
    }

    public void sknp(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;

        if (!keyboard[chip8.V[targetVReg]]) {
            chip8.pcReg += 4;
        } else {
            chip8.pcReg += 2;
        }
    }

    public void ldVxDT(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;

        chip8.V[targetVReg] = delayTimer;
        chip8.pcReg += 2;
    }

    public void ldVxK(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;
        chip8.wasKeyPressed = FALSE;

        for (int i = 0; i < NUM_KEYS; i++) {
            if (keyboard[i]) {
                chip8.V[targetVReg] = i;
                chip8.wasKeyPressed = TRUE;
            }
        }

        if (chip8.wasKeyPressed) {
            chip8.pcReg += 2;
        }
    }

    public void ldDTVx(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;

        chip8.delayTimer = chip8.V[targetVReg];
        chip8.pcReg += 2;
    }

    public void ldSTVx(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;

        chip8.soundTimer = chip8.V[targetVReg];
        chip8.pcReg += 2;
    }

    public void addIVx(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;

        chip8.IReg += chip8.V[targetVReg];
        chip8.pcReg += 2;
    }

    public void ldFVx(Chip8 chip8) {
        int targetVReg = (chip8.currentOp & 0x0F00) >> 8;

        chip8.IReg = chip8.V[targetVReg] * 5;
        chip8.pcReg += 2;
    }

    public void updateTimers(Chip8 chip8) {
        if (chip8.delayTimer > 0) {
            chip8.delayTimer--;
        }
        if (chip8.soundTimer > 0) {
            chip8.soundTimer--;
        }
    }
}
