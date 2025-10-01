import React, { useState } from 'react';
import { Upload, X, User } from 'lucide-react';

const ImageUpload = ({ type, id, currentImage, onImageUpdate }) => {
  const [uploading, setUploading] = useState(false);
  const [preview, setPreview] = useState(currentImage);

  const handleFileSelect = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    // Validate file type
    if (!file.type.startsWith('image/')) {
      alert('Please select an image file');
      return;
    }

    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
      alert('File size must be less than 5MB');
      return;
    }

    setUploading(true);

    try {
      const formData = new FormData();
      formData.append('image', file);

      const endpoint = type === 'student' 
        ? `http://localhost:8081/api/images/student/${id}`
        : `http://localhost:8081/api/images/user/${id}`;

      const response = await fetch(endpoint, {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();
        setPreview(`http://localhost:8081${data.imagePath}`);
        if (onImageUpdate) {
          onImageUpdate(data.imagePath);
        }
      } else {
        throw new Error('Upload failed');
      }
    } catch (error) {
      console.error('Error uploading image:', error);
      alert('Failed to upload image');
    } finally {
      setUploading(false);
    }
  };

  const removeImage = () => {
    setPreview(null);
    if (onImageUpdate) {
      onImageUpdate(null);
    }
  };

  return (
    <div className="flex flex-col items-center space-y-3">
      <div className="relative">
        {preview ? (
          <div className="relative">
            <img
              src={preview}
              alt={`${type} profile`}
              className="w-24 h-24 rounded-full object-cover border-2 border-gray-200"
            />
            <button
              onClick={removeImage}
              className="absolute -top-2 -right-2 bg-red-500 text-white rounded-full p-1 hover:bg-red-600"
            >
              <X className="w-3 h-3" />
            </button>
          </div>
        ) : (
          <div className="w-24 h-24 rounded-full bg-gray-100 border-2 border-dashed border-gray-300 flex items-center justify-center">
            <User className="w-8 h-8 text-gray-400" />
          </div>
        )}
      </div>

      <div className="flex flex-col items-center space-y-2">
        <label className="cursor-pointer">
          <input
            type="file"
            accept="image/*"
            onChange={handleFileSelect}
            className="hidden"
            disabled={uploading}
          />
          <div className="flex items-center space-x-2 px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:bg-gray-400">
            <Upload className="w-4 h-4" />
            <span>{uploading ? 'Uploading...' : 'Upload Photo'}</span>
          </div>
        </label>
        <p className="text-xs text-gray-500">Max 5MB, JPG/PNG</p>
      </div>
    </div>
  );
};

export default ImageUpload;