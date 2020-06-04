function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
% Instructions: You should complete the code by working through the
%               following parts.
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%

X_new = [ones(m, 1) X];
L1 = Theta1 * X_new';
a1 = sigmoid(L1);

[temp1, temp2] = size(a1);
a1_new = [ones(1, temp2); a1];
L2 = Theta2 * a1_new;
a2 = sigmoid(L2);

y_copy = zeros(num_labels, m);

for i = 1:m
    y_copy(y(i), i) = 1;
end

J = -(1/m)*sum(sum(y_copy.*log(a2) + (1-y_copy).*log(1-a2)));

Theta1_new = Theta1(:, 2:end) .* Theta1(:, 2:end);
Theta2_new = Theta2(:, 2:end) .* Theta2(:, 2:end);

J = J + (lambda/(2*m))*(sum(sum(Theta1_new)) + sum(sum(Theta2_new)));



delta_1 = zeros(size(Theta1));
delta_2 = zeros(size(Theta2));
for t = 1:m
   X_s = X_new(t, :);% 1 401
   z2 = Theta1 * X_s';% 25 1
   a2 = sigmoid(z2); %25 1

   [temp1, temp2] = size(z2);
   z2_new = [ones(1, temp2); z2]; % 26 1
   [temp1, temp2] = size(a2);
   a2_new = [ones(1, temp2); a2]; % 26 1
   
   z3 = Theta2 * a2_new; %10 1
   a3 = sigmoid(z3);% 10 1
   
   d_3 = a3 - y_copy(:, t); % 10 1
   d_2 = (Theta2'*d_3) .* sigmoidGradient(z2_new); % 26 1
   
   delta_2 = delta_2 + d_3*a2_new';% 10 26
   d_2 = d_2(2:end);
   delta_1 = delta_1 + d_2 * X_s;%25 401

end

Theta1_grad = (1/m) * delta_1;
Theta2_grad = (1/m) * delta_2;



[row, col] = size(Theta1);

re1 = Theta1(:, 2:end);
rel1 =  [zeros(row, 1) re1];
Theta1_grad = Theta1_grad + (lambda / m) * rel1;

[row, col] = size(Theta2);

re1 = Theta2(:, 2:end);
rel1 =  [zeros(row, 1) re1];
Theta2_grad = Theta2_grad + (lambda / m) * rel1;



% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end