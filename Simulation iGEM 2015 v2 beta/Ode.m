function [ D ] = Ode(varargin)% t,M )
%ODE Summary of this function goes here
%   Detailed explanation goes here
%[C,V,U,A,B,N_max]=setDefConst();
% globalisation may be problematic in the gui
% varargin = t, M, C,V,U,A,B,N_max
 persistent C V U A B N_max;
if nargin > 2
   
    C=varargin{3};
    V=varargin{4};
    U=varargin{5};
    A=varargin{6};
    B=varargin{7};
    N_max=varargin{8};
    
else
M=varargin{2};
D=derive(M,C,V,U,A,B,N_max);
D=D.';
end
end

