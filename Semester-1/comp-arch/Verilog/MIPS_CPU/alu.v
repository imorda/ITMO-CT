module alu(srca, srcb, alucontrol, aluresult, zero);
  input [31:0] srca, srcb;
  input [2:0] alucontrol;
  output reg [31:0] aluresult;
  output reg zero;
  
  initial begin
    aluresult=calculate(srca, srcb, alucontrol);
  end
  
  always @(srca or srcb or alucontrol) begin
    aluresult=calculate(srca, srcb, alucontrol);
  end
  
  function [31:0] calculate;
    input signed [31: 0] srca, srcb;
    input[2:0] alucontrol;
    begin
      case(alucontrol)
        3'b010: calculate=srca + srcb; //add
        3'b110: calculate=srca - srcb; //subtract
        3'b000: calculate=srca & srcb; //and
        3'b001: calculate=srca | srcb; //or
        3'b111: calculate=srca < srcb; //set less than
        default: calculate=0;
      endcase
      zero = calculate == 0;
    end
  endfunction
endmodule
