module control_unit(opcode, funct, memtoreg, memwrite, branch, alusrc, regdst, regwrite, alucontrol);
  input [5:0] opcode, funct;
  output reg memtoreg, memwrite, branch, alusrc, regdst, regwrite;
  output reg [2:0] alucontrol;

  initial begin
    alucontrol=decode(opcode, funct);
  end
  
  always @(opcode or funct) begin
    alucontrol=decode(opcode, funct);
  end
  
  function [2:0] decode;
    input[5: 0] opcode, funct;
    reg[1:0] aluop;
    begin
      case(opcode)  //Main decoder
        6'b000000: begin //R-type
          aluop=2'b10;
          regwrite=1;
          regdst=1;
          alusrc=0;
          branch=0;
          memwrite=0;
          memtoreg=0;
        end
        
        6'b001000: begin //addi
          aluop=2'b00;
          regwrite=1;
          regdst=0;
          alusrc=1;
          branch=0;
          memwrite=0;
          memtoreg=0;
        end
        
        6'b100011: begin //lw
          aluop=2'b00;
          regwrite=1;
          regdst=0;
          alusrc=1;
          branch=0;
          memwrite=0;
          memtoreg=1;
        end
        
        6'b101011: begin //sw
          aluop=2'b00;
          regwrite=0;
          regdst=0; //dc
          alusrc=1;
          branch=0;
          memwrite=1;
          memtoreg=0; //dc
        end
        
        6'b000100: begin //beq
          aluop=2'b01;
          regwrite=0;
          regdst=0; //dc
          alusrc=0;
          branch=1;
          memwrite=0;
          memtoreg=0; //dc
        end
        
        default: begin
          aluop=0; //dc
          regwrite=0; //dc
          regdst=0; //dc
          alusrc=0; //dc
          branch=0; //dc
          memwrite=0; //dc
          memtoreg=0; //dc
        end
      endcase
      
      
      casex(aluop)  //ALU decoder
        2'b00: decode=3'b010; //add
        2'bx1: decode=3'b110; //subtract
        2'b1x: begin
          case(funct)
            6'b100000: decode=3'b010; //add
            6'b100010: decode=3'b110; //sub
            6'b100100: decode=3'b000; //and
            6'b100101: decode=3'b001; //or
            6'b101010: decode=3'b111; //slt
            default: decode=3'b100; //unused
          endcase
        end
       	default: decode=3'b100; //unused
      endcase
    end
  endfunction
endmodule
